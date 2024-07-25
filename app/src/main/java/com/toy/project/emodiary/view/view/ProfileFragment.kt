package com.toy.project.emodiary.view.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.FragmentProfileBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.model.dto.Emotions
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val diaryViewModel: DiaryViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    // 요일별 감정
    private val emotions = listOf(3, 4, 2, 5, 3, 1, 4)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupChart()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.btnSignOut.setOnClickListener {
            authViewModel.signOut()
        }
    }

    private fun setupChart() {
        binding.lineChart.apply {
            extraTopOffset = 30f
            extraBottomOffset = 20f
            extraLeftOffset = 10f
            extraRightOffset = 20f
            description.isEnabled = false
            legend.isEnabled = false // 범례
            axisRight.isEnabled = false // 오른쪽
            setPinchZoom(false)
            setTouchEnabled(false)
        }

        // 아래쪽
        binding.lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            spaceMin = 0.3f
            spaceMax = 0.3f
            textSize = 16f
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(emptyArray())
        }

        // 왼쪽
        binding.lineChart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 5f
            granularity = 1f
            spaceMin = 0.3f
            spaceMax = 0.3f
            textSize = 16f
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(arrayOf("", "나쁨", "", "보통", "", "좋음"))
        }
    }

    private fun setupChartData(emotions: List<Emotions>) {
        val entries = emotions.mapIndexed { index, emotion ->
            Entry(index.toFloat(), emotion.emotion.toFloat())
        }

        val xAxisValues = emotions.map { it.day }.toTypedArray()
        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        // 선
        val dataSet = LineDataSet(entries, "감정 변화").apply {
            color = resources.getColor(R.color.light_blue, null)
            lineWidth = 4f
            setDrawCircles(true)
            circleRadius = 6f
            setCircleColor(resources.getColor(R.color.blue, null))
            setDrawCircleHole(true)
            circleHoleRadius = 3f
            circleHoleColor = Color.WHITE
            setDrawValues(false)
            setDrawHighlightIndicators(false)
        }

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            getMyInformation()

            myInfo.observe(viewLifecycleOwner) {
                binding.txtNickname.text = it.nickname
                binding.txtFirstWritten.text = if (it.firstDiaryDate == null) {
                    "첫 일기를 작성해보세요!"
                } else {
                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
                    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREA)
                    val date = LocalDate.parse(it.firstDiaryDate, inputFormatter)
                    date.format(outputFormatter)
                }
                binding.txtProgress.text = "${it.percentage}%"
                binding.progressIndicator.progress = it.percentage.toFloat().toInt()

                if (it.emotions.isNotEmpty())
                    setupChartData(it.emotions)
            }
        }

        authViewModel.apply {
            signOut.observe(viewLifecycleOwner) {
                dataStoreViewModel.deleteAccessToken()
                dataStoreViewModel.deleteRefreshToken()
                UserData.clearUserData()
                startActivity(Intent(requireContext(), SignInActivity::class.java)).also { activity?.finish() }
            }
        }
    }
}