package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.toy.project.emodiary.databinding.FragmentHomeBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.adapter.DiaryListAdapter
import com.toy.project.emodiary.view.adapter.Month
import com.toy.project.emodiary.view.adapter.MonthListAdapter
import com.toy.project.emodiary.view.adapter.Year
import com.toy.project.emodiary.view.adapter.YearListAdapter
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val diaryViewModel: DiaryViewModel by viewModels()

    private val yearListAdapter = YearListAdapter()
    private val monthListAdapter = MonthListAdapter()
    private val diaryListAdapter = DiaryListAdapter()

    private val currentYear = LocalDate.now().year
    private val currentMonth = LocalDate.now().monthValue
    private var year = currentYear
    private var month = currentMonth
    private var months = (1..12).map { month ->
        Month(month, month == currentMonth)
    }
    private var isFirstTime = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstTime)
            diaryViewModel.getDiaryList(currentYear, currentMonth)
        else
            diaryViewModel.getDiaryList(year, month)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerYear.apply {
            adapter = yearListAdapter
            itemAnimator = null
        }

        binding.recyclerMonth.apply {
            adapter = monthListAdapter
            itemAnimator = null
        }

        binding.recyclerDiary.apply {
            adapter = diaryListAdapter
            itemAnimator = null
        }

        yearListAdapter.setOnItemClickListener { item, _ ->
            diaryViewModel.apply {
                setCurrentYear(item.year)
                getDiaryList(item.year, this@HomeFragment.month)
                if (this@HomeFragment.isFirstTime) setIsFirstTime(false)
            }
        }

        monthListAdapter.setOnItemClickListener { item, _ ->
            diaryViewModel.apply {
                setCurrentMonth(item.month)
                getDiaryList(this@HomeFragment.year, item.month)
                if (this@HomeFragment.isFirstTime) setIsFirstTime(false)
            }
        }

        diaryListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), DiaryActivity::class.java)
            intent.putExtra("date", item.createdDate)
            intent.putExtra("title", item.title)
            intent.putExtra("content", item.content)
            intent.putExtra("emotion", item.emotionUrl)
            intent.putExtra("weather", item.weatherUrl)
            intent.putExtra("wordCloud", item.wordCloudUrl)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            diaryList.observe(viewLifecycleOwner) {
                binding.progressIndicator.hide()
                binding.scrollView.visibility = View.VISIBLE

                binding.txtName.text = "${UserData.nickname} 님"
                binding.txtGreeting.text = "오늘 하루는 어떠셨나요?" // 임시 텍스트

                yearListAdapter.apply {
                    if (it.years.isEmpty()) {
                        setItemList(listOf(Year(this@HomeFragment.currentYear, 0, true)))
                    } else {
                        setItemList(it.years.map { year -> Year(year.year, year.count, year.year == this@HomeFragment.currentYear) })
                        setItemSelect(it.years.indexOfFirst { year -> year.year == this@HomeFragment.year})
                    }
                }
                monthListAdapter.setItemList(months)
                diaryListAdapter.setItemList(it.diary)

                if (this@HomeFragment.isFirstTime)
                    binding.recyclerMonth.scrollToPosition(this@HomeFragment.currentMonth - 1)
                binding.txtNoDiary.visibility = if (it.diary.isEmpty()) View.VISIBLE else View.GONE
            }

            currentYear.observe(viewLifecycleOwner) { year ->
                this@HomeFragment.year = year
            }

            currentMonth.observe(viewLifecycleOwner) { month ->
                this@HomeFragment.month = month
                months = (1..12).map { m ->
                    Month(m, m == month)
                }
            }

            isFirstTime.observe(viewLifecycleOwner) { isFirstTime ->
                this@HomeFragment.isFirstTime = isFirstTime
            }
        }
    }
}