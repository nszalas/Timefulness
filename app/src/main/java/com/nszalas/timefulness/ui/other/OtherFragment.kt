package com.nszalas.timefulness.ui.other

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.nszalas.timefulness.R
import com.nszalas.timefulness.databinding.FragmentOtherBinding
import com.nszalas.timefulness.extensions.collectOnViewLifecycle
import com.nszalas.timefulness.utils.LeadingMargin
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class OtherFragment : Fragment() {

    private var _binding: FragmentOtherBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OtherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectOnViewLifecycle(viewModel.state, ::onNewState)
        viewModel.onRefresh()
        setupPomodoroCard()
    }

    private fun setupPomodoroCard() {
        with(binding.pomodoroCard.pomodoroTimer) {
            regularLengthButton.setOnClickListener { viewModel.onTimerStatusChanged(PomodoroStatus.Regular()) }
            shortBreakButton.setOnClickListener { viewModel.onTimerStatusChanged(PomodoroStatus.ShortBreak()) }
            longBreakButton.setOnClickListener { viewModel.onTimerStatusChanged(PomodoroStatus.LongBreak()) }
            startButton.setOnClickListener { viewModel.startCountdown() }
        }
    }

    private fun onNewState(state: OtherViewState) {
        with(binding.adviceCard) {
            root.isVisible = state.advice != null

            this.description.text = state.advice?.description
        }

        with(binding.techniqueCard) {
            root.isVisible = state.technique != null

            this.title.text = state.technique?.title

            val image: Drawable? = getDrawable(requireContext(), R.drawable.ic_person_at_desk)
            val spannable: SpannableString? = state.technique?.let { SpannableString(it.description) }

            image?.let {
                val margin = image.intrinsicWidth + 12
                spannable?.setSpan(LeadingMargin(margin, 4), 0, spannable.length ?: 0, 0)
            }

            this.description.text = spannable
        }

        with(binding.pomodoroCard.pomodoroTimer) {
            val formatter: NumberFormat = DecimalFormat("00")
            val timer = Pair(
                first = state.timeLeft / 60,
                second = state.timeLeft % 60
            )
            minutesTextView.text = formatter.format(timer.first)
            secondsTextView.text = formatter.format(timer.second)
            startButton.isEnabled = state.pomodoroStatus !is PomodoroStatus.CountingDown
        }
    }

}