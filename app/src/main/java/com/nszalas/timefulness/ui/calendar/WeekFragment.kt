package com.nszalas.timefulness.ui.calendar

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Range
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nszalas.timefulness.R
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getDateTimeFromTS
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getDayCodeFromDateTime
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getUTCDateTimeFromTS
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getUTCDayCodeFromTS
import com.nszalas.timefulness.ui.calendar.utils.WEEK_START_TIMESTAMP
import com.nszalas.timefulness.ui.calendar.utils.checkViewStrikeThrough
import com.nszalas.timefulness.ui.calendar.utils.getWeeklyViewItemHeight
import com.nszalas.timefulness.ui.calendar.utils.intersects
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.*
import com.simplemobiletools.commons.views.MyTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_week.*
import kotlinx.android.synthetic.main.fragment_week.view.*
import kotlinx.android.synthetic.main.week_event_marker.view.*
import org.joda.time.DateTime
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

private const val DAYS_ON_PAGE = 7

@AndroidEntryPoint
class WeekFragment : Fragment(), WeeklyCalendar {

    private val viewModel by viewModels<WeekFragmentViewModel>()

    var listener: WeekFragmentListener? = null
    private var weekTimestamp = 0L
    private var rowHeight = 0f
    private var todayColumnIndex = -1
    private var primaryColor = 0
    private var lastHash = 0
    private var defaultRowHeight = 0f
    private var mWasDestroyed = false
    private var isFragmentVisible = false
    private var wasFragmentInit = false
    private var wasExtraHeightAdded = false
    private var currentTimeView: ImageView? = null
    private var allDayHolders = ArrayList<RelativeLayout>()
    private var allDayRows = ArrayList<HashSet<Int>>()
    private var allDayEventToRow = LinkedHashMap<Event, Int>()
    private var dayColumns = ArrayList<RelativeLayout>()
    private var eventTimeRanges = LinkedHashMap<String, LinkedHashMap<Long, EventWeeklyView>>()

    private lateinit var inflater: LayoutInflater
    private lateinit var mView: View
    private lateinit var scrollView: MyScrollView
    private lateinit var res: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        res = requireContext().resources
        rowHeight = requireContext().getWeeklyViewItemHeight()
        defaultRowHeight = res.getDimension(R.dimen.weekly_view_row_height)
        weekTimestamp = requireArguments().getLong(WEEK_START_TIMESTAMP)
        primaryColor = requireContext().getProperPrimaryColor()
        allDayRows.add(HashSet())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.inflater = inflater

        val fullHeight = requireContext().getWeeklyViewItemHeight().toInt() * 24
        mView = inflater.inflate(R.layout.fragment_week, container, false).apply {
            scrollView = week_events_scrollview
            week_horizontal_grid_holder.layoutParams.height = fullHeight
            week_events_columns_holder.layoutParams.height = fullHeight
        }

        addDayColumns()
        scrollView.setOnScrollviewListener(object : MyScrollView.ScrollViewListener {
            override fun onScrollChanged(
                scrollView: MyScrollView,
                x: Int,
                y: Int,
                oldx: Int,
                oldy: Int
            ) {
                checkScrollLimits(y)
            }
        })

        scrollView.onGlobalLayout {
            if (fullHeight < scrollView.height) {
                scrollView.layoutParams.height =
                    fullHeight - res.getDimension(R.dimen.one_dp).toInt()
            }
            // if its just past midnight we don't want the scroll to be negative :)
            val startHour = max(DateTime.now().hourOfDay - 1, 0)

            val initialScrollY = (rowHeight * startHour).toInt()
            updateScrollY(max(listener?.getCurrScrollY() ?: 0, initialScrollY))
        }
        observeViewModelEvents()
        wasFragmentInit = true
        return mView
    }

    override fun onResume() {
        super.onResume()

        setupDayLabels()
        updateCalendar()

        if (rowHeight != 0f && mView.width != 0) {
            addCurrentTimeIndicator()
        }
    }

    override fun onPause() {
        super.onPause()
        wasExtraHeightAdded = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mWasDestroyed = true
    }

    // without it the hour column scroll doesn't update
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        isFragmentVisible = menuVisible
        if (isFragmentVisible && wasFragmentInit) {
            listener?.updateHoursTopMargin(mView.week_top_holder.height)
            checkScrollLimits(scrollView.scrollY)

            // fix some glitches like at swiping from a fully scaled out fragment with all-day events to an empty one
            val fullFragmentHeight =
                (listener?.getFullFragmentHeight() ?: 0) - mView.week_top_holder.height
            if (scrollView.height < fullFragmentHeight) {
                updateViewScale()
                listener?.updateRowHeight(rowHeight.toInt())
            }
        }
    }

    private fun observeViewModelEvents() {
        viewModel.event.observe(viewLifecycleOwner) { viewModelEvent ->
            when (viewModelEvent) {
                is WeekFragmentEvent.EventsLoaded -> {
                    updateWeeklyCalendar(viewModelEvent.tasks)
                }
                WeekFragmentEvent.EventsUpdated -> updateCalendar()
            }
        }
    }

    fun updateCalendar() {
        viewModel.getEvents(weekTimestamp)
    }

    private fun addDayColumns() {
        mView.week_events_columns_holder.removeAllViews()
        (0 until DAYS_ON_PAGE).forEach {
            val column = inflater.inflate(
                R.layout.weekly_view_day_column,
                mView.week_events_columns_holder,
                false
            ) as RelativeLayout
            column.tag = getUTCDayCodeFromTS(weekTimestamp + it * DAY_SECONDS)
            mView.week_events_columns_holder.addView(column)
            dayColumns.add(column)
        }
    }

    // put labels in correct columns and set different color if a column is today
    @SuppressLint("SetTextI18n")
    private fun setupDayLabels() {
        var curDay = getUTCDateTimeFromTS(weekTimestamp)
        val todayCode = getDayCodeFromDateTime(DateTime())

        mView.week_letters_holder.removeAllViews()
        for (i in 0 until DAYS_ON_PAGE) {
            val dayCode = getDayCodeFromDateTime(curDay)
            val labelIDs = R.array.week_days_short

            val dayLetters = res.getStringArray(labelIDs).toMutableList() as ArrayList<String>
            val dayLetter = dayLetters[curDay.dayOfWeek - 1]

            val textColor = if (todayCode == dayCode) {
                primaryColor
            } else {
                requireContext().getProperTextColor()
            }

            val label = inflater.inflate(
                R.layout.weekly_view_day_letter,
                mView.week_letters_holder,
                false
            ) as MyTextView
            label.text = "$dayLetter\n${curDay.dayOfMonth}"
            label.setTextColor(textColor)
            if (todayCode == dayCode) {
                todayColumnIndex = i
            }

            mView.week_letters_holder.addView(label)
            curDay = curDay.plusDays(1)
        }
    }

    private fun checkScrollLimits(y: Int) {
        if (isFragmentVisible) {
            listener?.scrollTo(y)
        }
    }

    private fun initGrid() {
        (0 until DAYS_ON_PAGE).mapNotNull { dayColumns.getOrNull(it) }
            .forEachIndexed { _, layout ->
                layout.removeAllViews()
            }
    }

    override fun updateWeeklyCalendar(tasks: ArrayList<TaskWithCategoryUI>) {
        val newHash = tasks.hashCode()
        if (newHash == lastHash || mWasDestroyed || context == null) {
            return
        }

        lastHash = newHash

        requireActivity().runOnUiThread {
            if (context != null && activity != null && isAdded) {
                addEvents(tasks)
            }
        }
    }

    private fun updateViewScale() {
        rowHeight = context?.getWeeklyViewItemHeight() ?: return

        val oneDp = res.getDimension(R.dimen.one_dp).toInt()
        val fullHeight = max(rowHeight.toInt() * 24, scrollView.height + oneDp)
        scrollView.layoutParams.height = fullHeight - oneDp
        mView.week_horizontal_grid_holder.layoutParams.height = fullHeight
        mView.week_events_columns_holder.layoutParams.height = fullHeight
    }

    @SuppressLint("InflateParams")
    private fun addEvents(tasksWithCategories: ArrayList<TaskWithCategoryUI>) {
        initGrid()
        allDayHolders.clear()
        allDayRows.clear()
        eventTimeRanges.clear()
        allDayRows.add(HashSet())
        week_all_day_holder?.removeAllViews()
        addNewLine()
        allDayEventToRow.clear()

        val minuteHeight = rowHeight / 60
        val minimalHeight = res.getDimension(R.dimen.weekly_view_minimal_event_height).toInt()
        val density = res.displayMetrics.density.roundToInt()

        tasksWithCategories.forEach { list ->
            val startDateTime = getDateTimeFromTS(list.task.startTimestamp)
            val endDateTime = getDateTimeFromTS(list.task.endTimestamp)

            val currentDayCode = getDayCodeFromDateTime(startDateTime)

            val startMinutes = startDateTime.minuteOfDay
            val duration = endDateTime.minuteOfDay - startMinutes

            var endMinutes = startMinutes + duration
            if ((endMinutes - startMinutes) * minuteHeight < minimalHeight) {
                endMinutes = startMinutes + (minimalHeight / minuteHeight).toInt()
            }

            val range = Range(startMinutes, endMinutes)
            val eventWeekly = EventWeeklyView(range)

            if (!eventTimeRanges.containsKey(currentDayCode)) {
                eventTimeRanges[currentDayCode] = LinkedHashMap()
            }
            eventTimeRanges[currentDayCode]?.put(list.task.id.toLong(), eventWeekly)
        }

        eventTimeRanges.forEach { (_, eventDayList) ->
            val eventsCollisionChecked = ArrayList<Long>()
            eventDayList.forEach { (eventId, eventWeeklyView) ->
                if (eventWeeklyView.slot == 0) {
                    eventWeeklyView.slot = 1
                    eventWeeklyView.slot_max = 1
                }
                eventsCollisionChecked.add(eventId)
                val eventWeeklyViewsToCheck =
                    eventDayList.filterNot { eventsCollisionChecked.contains(it.key) }

                eventWeeklyViewsToCheck.forEach { (toCheckId, eventWeeklyViewToCheck) ->
                    val areTouching = eventWeeklyView.range.intersects(eventWeeklyViewToCheck.range)
                    val doHaveCommonMinutes = if (areTouching) {
                        eventWeeklyView.range.upper > eventWeeklyViewToCheck.range.lower || (eventWeeklyView.range.lower == eventWeeklyView.range.upper &&
                                eventWeeklyView.range.upper == eventWeeklyViewToCheck.range.lower)
                    } else {
                        false
                    }

                    if (areTouching && doHaveCommonMinutes) {
                        if (eventWeeklyViewToCheck.slot == 0) {
                            val nextSlot = eventWeeklyView.slot_max + 1
                            val slotRange = Array(eventWeeklyView.slot_max) { it + 1 }
                            val collisionEventWeeklyViews =
                                eventDayList.filter { eventWeeklyView.collisions.contains(it.key) }

                            for ((_, collisionEventWeeklyView) in collisionEventWeeklyViews) {
                                if (collisionEventWeeklyView.range.intersects(eventWeeklyViewToCheck.range)) {
                                    slotRange[collisionEventWeeklyView.slot - 1] = nextSlot
                                }
                            }
                            slotRange[eventWeeklyView.slot - 1] = nextSlot
                            val slot = slotRange.minOrNull()
                            eventWeeklyViewToCheck.slot = slot!!
                            if (slot == nextSlot) {
                                eventWeeklyViewToCheck.slot_max = nextSlot
                                eventWeeklyView.slot_max = nextSlot
                                for ((_, collisionEventWeeklyView) in collisionEventWeeklyViews) {
                                    collisionEventWeeklyView.slot_max++
                                }
                            } else {
                                eventWeeklyViewToCheck.slot_max = eventWeeklyView.slot_max
                            }
                        }
                        eventWeeklyView.collisions.add(toCheckId)
                        eventWeeklyViewToCheck.collisions.add(eventId)
                    }
                }
            }
        }

        dayevents@ for (taskWithCategory in tasksWithCategories) {
            val startDateTime = getDateTimeFromTS(taskWithCategory.task.startTimestamp)

            val currentDayCode = getDayCodeFromDateTime(startDateTime)

            val dayOfWeek = dayColumns.indexOfFirst { it.tag == currentDayCode }
            if (dayOfWeek == -1 || dayOfWeek >= DAYS_ON_PAGE) {
                continue@dayevents
            }

            val dayColumn = dayColumns[dayOfWeek]
            (inflater.inflate(
                R.layout.week_event_marker,
                null,
                false
            ) as LinearLayout).apply {
                val backgroundColor = if (taskWithCategory.task.completed) {
                    Color.GRAY
                } else {
                    taskWithCategory.category.colorMain
                }.adjustAlpha(MEDIUM_ALPHA)
                val textColor = backgroundColor.getContrastColor().adjustAlpha(HIGHER_ALPHA)
                val currentEventWeeklyView =
                    eventTimeRanges[currentDayCode]!![taskWithCategory.task.id.toLong()]

                background = ColorDrawable(backgroundColor)

                dayColumn.addView(this)
                y = currentEventWeeklyView!!.range.lower * minuteHeight

                week_event_label.apply {
                    setTextColor(textColor)
                    maxLines =
                        if (taskWithCategory.task.startTimestamp == taskWithCategory.task.endTimestamp) {
                            1
                        } else {
                            3
                        }

                    text = taskWithCategory.task.title
                    checkViewStrikeThrough(taskWithCategory.task.completed)
                    contentDescription = text

                    minHeight =
                        if (taskWithCategory.task.startTimestamp == taskWithCategory.task.endTimestamp) {
                            minimalHeight
                        } else {
                            ((currentEventWeeklyView.range.upper - currentEventWeeklyView.range.lower) * minuteHeight).toInt() - 1
                        }
                }

                (layoutParams as RelativeLayout.LayoutParams).apply {
                    width = (dayColumn.width - 1) / currentEventWeeklyView.slot_max
                    x = (width * (currentEventWeeklyView.slot - 1)).toFloat()
                    if (currentEventWeeklyView.slot > 1) {
                        x += density
                        width -= density
                    }
                }
                setOnClickListener { listener?.eventSingleTouch(taskWithCategory) }
            }
        }

        checkTopHolderHeight()
        addCurrentTimeIndicator()
    }

    @SuppressLint("InflateParams")
    private fun addNewLine() {
        val allDaysLine =
            inflater.inflate(R.layout.all_day_events_holder_line, null, false) as RelativeLayout
        week_all_day_holder?.addView(allDaysLine)
        allDayHolders.add(allDaysLine)
    }

    @SuppressLint("InflateParams")
    private fun addCurrentTimeIndicator() {
        if (todayColumnIndex != -1) {
            val calendar = Calendar.getInstance()
            val minutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
            if (todayColumnIndex >= dayColumns.size) {
                currentTimeView?.alpha = 0f
                return
            }

            if (currentTimeView != null) {
                mView.week_events_holder.removeView(currentTimeView)
            }

            currentTimeView =
                (inflater.inflate(R.layout.week_now_marker, null, false) as ImageView).apply {
                    applyColorFilter(primaryColor)
                    mView.week_events_holder.addView(this, 0)
                    val extraWidth = res.getDimension(R.dimen.activity_margin).toInt()
                    val markerHeight = res.getDimension(R.dimen.weekly_view_now_height).toInt()
                    val minuteHeight = rowHeight / 60
                    (layoutParams as RelativeLayout.LayoutParams).apply {
                        width = (mView.width / DAYS_ON_PAGE) + extraWidth
                        height = markerHeight
                    }

                    x = if (DAYS_ON_PAGE == 1) {
                        0f
                    } else {
                        (mView.width / DAYS_ON_PAGE * todayColumnIndex).toFloat() - extraWidth / 2f
                    }

                    y = minutes * minuteHeight - markerHeight / 2
                }
        }
    }

    private fun checkTopHolderHeight() {
        mView.week_top_holder.onGlobalLayout {
            if (isFragmentVisible && activity != null && !mWasDestroyed) {
                listener?.updateHoursTopMargin(mView.week_top_holder.height)
            }
        }
    }

    fun updateScrollY(y: Int) {
        if (wasFragmentInit) {
            scrollView.scrollY = y
        }
    }

    fun updateNotVisibleViewScaleLevel() {
        if (!isFragmentVisible) {
            updateViewScale()
        }
    }
}
