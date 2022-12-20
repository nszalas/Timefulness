package com.nszalas.timefulness.ui.calendar

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentCalendarBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.ui.calendar.calendar.MyFragmentHolder
import com.nszalas.timefulness.ui.calendar.calendar.MyScrollView
import com.nszalas.timefulness.ui.calendar.calendar.MyWeekPagerAdapter
import com.nszalas.timefulness.ui.calendar.calendar.WeekFragmentListener
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getDateTimeFromTS
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getDayCodeFromTS
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getHours
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getMonthName
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getTodayCode
import com.nszalas.timefulness.ui.calendar.utils.Formatter.getUTCDateTimeFromTS
import com.nszalas.timefulness.ui.calendar.utils.getDatesWeekDateTime
import com.nszalas.timefulness.ui.calendar.utils.getWeeklyViewItemHeight
import com.nszalas.timefulness.ui.calendar.utils.seconds
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.WEEK_SECONDS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

private const val PREFILLED_WEEKS = 151
private const val DAYS_ON_PAGE = 7

@AndroidEntryPoint
class CalendarFragment : MyFragmentHolder(), WeekFragmentListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CalendarViewModel>()

    private val viewPager: ViewPager by lazy {
        binding.weekViewHolder.weekViewViewPager
    }
    private val weekHolder: ViewGroup by lazy {
        binding.weekViewHolder
    }

    private var defaultWeeklyPage = 0
    private var thisWeekTS = 0L
    private var currentWeekTS = 0L
    private var isGoToTodayVisible = false
    private var weekScrollY = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return getPersistentView(_binding)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.event, ::onNewEvent)

        val itemHeight = requireContext().getWeeklyViewItemHeight().toInt()
        binding.weekViewHolder.background =
            ColorDrawable(requireContext().getProperBackgroundColor())
        binding.weekViewHolder.weekViewHoursHolder.setPadding(0, 0, 0, itemHeight)

        val dateTimeString = getDatesWeekDateTime(DateTime())
        currentWeekTS = (DateTime.parse(dateTimeString) ?: DateTime()).seconds()
        thisWeekTS = DateTime.parse(getDatesWeekDateTime(DateTime())).seconds()

        viewPager.id = (System.currentTimeMillis() % 100000).toInt()
        setupFragment()
    }

    private fun onNewEvent(event: CalendarViewEvent) {
        when (event) {
            is CalendarViewEvent.NavigateEditTask -> {
                findNavController().navigate(
                    CalendarFragmentDirections.actionNavigationCalendarToTaskDetailsDialogFragment(
                        task = event.task
                    )
                )
            }
            CalendarViewEvent.NavigateToAddTask -> {
                findNavController().navigate(
                    CalendarFragmentDirections.actionNavigationCalendarToAddTaskFragment()
                )
            }
        }
    }

    private fun setupFragment() {
        binding.addImage.setOnClickListener { viewModel.onAddTaskClicked() }
        addHours()
        setupWeeklyViewPager()
    }

    private fun setupWeeklyViewPager() {
        val weekTSs = getWeekTimestamps(currentWeekTS)
        val weeklyAdapter =
            MyWeekPagerAdapter(childFragmentManager, weekTSs, this)

        defaultWeeklyPage = weekTSs.size / 2

        viewPager.apply {
            adapter = weeklyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    currentWeekTS = weekTSs[position]
                    val shouldGoToTodayBeVisible = shouldGoToTodayBeVisible()
                    if (isGoToTodayVisible != shouldGoToTodayBeVisible) {
//                        (activity as? MainActivity)?.toggleGoToTodayVisibility(shouldGoToTodayBeVisible)
                        isGoToTodayVisible = shouldGoToTodayBeVisible
                    }

                    setupWeeklyActionbarTitle(weekTSs[position])
                }
            })
            currentItem = defaultWeeklyPage
        }

        weekHolder.weekViewHoursScrollview?.setOnScrollviewListener(object :
            MyScrollView.ScrollViewListener {
            override fun onScrollChanged(
                scrollView: MyScrollView,
                x: Int,
                y: Int,
                oldx: Int,
                oldy: Int
            ) {
                weekScrollY = y
                weeklyAdapter.updateScrollY(viewPager.currentItem, y)
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun addHours(textColor: Int = requireContext().getProperTextColor()) {
        val itemHeight = requireContext().getWeeklyViewItemHeight().toInt()
        weekHolder.weekViewHoursHolder?.removeAllViews()
        val hourDateTime = DateTime().withDate(2000, 1, 1).withTime(0, 0, 0, 0)
        for (i in 1..23) {
            val formattedHours = getHours(hourDateTime.withHourOfDay(i))
            (layoutInflater.inflate(
                R.layout.weekly_view_hour_textview,
                null,
                false
            ) as TextView).apply {
                text = formattedHours
                setTextColor(textColor)
                height = itemHeight
                weekHolder.weekViewHoursHolder?.addView(this)
            }
        }
    }

    private fun getWeekTimestamps(targetSeconds: Long): List<Long> {
        val weekTSs = ArrayList<Long>(PREFILLED_WEEKS)
        val dateTime = getDateTimeFromTS(targetSeconds)
        val shownWeekDays = DAYS_ON_PAGE
        var currentWeek = dateTime.minusDays(PREFILLED_WEEKS / 2 * shownWeekDays)
        for (i in 0 until PREFILLED_WEEKS) {
            weekTSs.add(currentWeek.seconds())
            currentWeek = currentWeek.plusDays(shownWeekDays)
        }
        return weekTSs
    }

    private fun setupWeeklyActionbarTitle(timestamp: Long) {
        val startDateTime = getDateTimeFromTS(timestamp)
        val endDateTime = getDateTimeFromTS(timestamp + WEEK_SECONDS)
        val startMonthName = getMonthName(requireContext(), startDateTime.monthOfYear)
        val monthsString = when (startDateTime.monthOfYear) {
            endDateTime.monthOfYear -> {
                startMonthName
            }
            else -> {
                val endMonthName = getMonthName(requireContext(), endDateTime.monthOfYear)
                "$startMonthName - $endMonthName"
            }
        }
        val weekString = "${getString(R.string.week)} ${startDateTime.plusDays(3).weekOfWeekyear}"
        val yearString = "${endDateTime.year}"
        binding.weekTextView.text = weekString
        binding.monthTextView.text = monthsString
        binding.yearTextView.text = yearString
    }

    override fun goToToday() {
        currentWeekTS = thisWeekTS
        setupFragment()
    }

    @SuppressLint("InflateParams")
    override fun showGoToDateDialog() {
        requireActivity().setTheme(requireContext().getDatePickerDialogTheme())
        val view = layoutInflater.inflate(R.layout.date_picker, null)
        val datePicker = view.findViewById<DatePicker>(R.id.date_picker)

        val dateTime = getCurrentDate()
        datePicker.init(dateTime.year, dateTime.monthOfYear - 1, dateTime.dayOfMonth, null)

        activity?.getAlertDialogBuilder()!!
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok) { _, _ -> dateSelected(dateTime, datePicker) }
            .apply {
                activity?.setupDialogStuff(view, this)
            }
    }

    private fun dateSelected(dateTime: DateTime, datePicker: DatePicker) {
        val isSundayFirst = Calendar.getInstance().firstDayOfWeek == Calendar.SUNDAY
        val month = datePicker.month + 1
        val year = datePicker.year
        val day = datePicker.dayOfMonth
        var newDateTime = dateTime.withDate(year, month, day)

        if (isSundayFirst) {
            newDateTime = newDateTime.plusDays(1)
        }

        var selectedWeek = newDateTime.withDayOfWeek(1).withTimeAtStartOfDay()
            .minusDays(if (isSundayFirst) 1 else 0)
        if (newDateTime.minusDays(7).seconds() > selectedWeek.seconds()) {
            selectedWeek = selectedWeek.plusDays(7)
        }

        currentWeekTS = selectedWeek.seconds()
        setupFragment()
    }

    override fun refreshEvents() {
        (viewPager.adapter as? MyWeekPagerAdapter)?.updateCalendars(viewPager.currentItem)
    }

    override fun shouldGoToTodayBeVisible() = currentWeekTS != thisWeekTS

    override fun getNewEventDayCode(): String {
        val currentTS = System.currentTimeMillis() / 1000
        return if (currentTS > currentWeekTS && currentTS < currentWeekTS + WEEK_SECONDS) {
            getTodayCode()
        } else {
            getDayCodeFromTS(currentWeekTS)
        }
    }

    override fun scrollTo(y: Int) {
        weekHolder.weekViewHoursScrollview?.scrollY = y
        weekScrollY = y
    }

    // gives the hour column a margin equal to the height of week days row
    override fun updateHoursTopMargin(margin: Int) {
        weekHolder.apply {
            weekViewHoursDivider?.layoutParams?.height = margin
            weekViewHoursScrollview?.requestLayout()
            weekViewHoursScrollview?.onGlobalLayout {
                weekViewHoursScrollview.scrollY = weekScrollY
            }
        }
    }

    override fun getCurrScrollY() = weekScrollY

    override fun updateRowHeight(rowHeight: Int) {
        val childCnt = weekHolder.weekViewHoursHolder?.childCount ?: return
        for (i in 0..childCnt) {
            val textView =
                weekHolder.weekViewHoursHolder?.getChildAt(i) as? TextView ?: continue
            textView.layoutParams.height = rowHeight
        }

        weekHolder.weekViewHoursHolder?.setPadding(0, 0, 0, rowHeight)
        (viewPager.adapter as? MyWeekPagerAdapter)?.updateNotVisibleScaleLevel(
            viewPager.currentItem
        )
    }

    override fun getFullFragmentHeight(): Int {
        val weekViewHolderHeight = weekHolder.weekViewHolder?.height ?: 0
        val weekViewDaysDividerHeight = weekHolder.weekViewDaysCountDivider?.height ?: 0
        return weekViewHolderHeight - weekViewDaysDividerHeight
    }

    override fun eventSingleTouch(task: TaskWithCategoryUI?) {
        task?.let { viewModel.onTaskClicked(it) }
    }

    override fun getCurrentDate(): DateTime = getUTCDateTimeFromTS(currentWeekTS)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}