package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toy.project.emodiary.databinding.FragmentHomeBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.adapter.Diary
import com.toy.project.emodiary.view.adapter.HomeAdapter
import com.toy.project.emodiary.view.adapter.HomeItem
import com.toy.project.emodiary.view.adapter.Month
import com.toy.project.emodiary.view.adapter.Year
import java.util.Calendar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter()

    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private val months = (1..12).map { month ->
        Month(month, month == currentMonth)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = homeAdapter

        val items = mutableListOf<HomeItem>()

        items.add(HomeItem.HomeHeader("오늘 하루는 어떠셨나요?", UserData.nickname ?: ""))

        items.add(HomeItem.HomeYear(listOf(
            Year(2024, 300, true),
            Year(2023, 200, false),
            Year(2022, 100, false),
        )))

        items.add(HomeItem.HomeMonth(months))

        items.add(HomeItem.HomeDiary(listOf(
            Diary(1, "2024-06-01", "행복한 하루", "오늘은 정말 행복한 하루였다. 아침에 일어나서 커피를 마시고, 오후에는 친구들과 즐거운 시간을 보냈다. 저녁에는 맛있는 저녁을 먹고 가족과 함께 시간을 보냈다."),
            Diary(2, "2024-06-17", "우울한 하루", "오늘은 정말 우울한 하루였다. 아침에 일어나서 기분이 좋지 않았고, 오후에는 친구들과 시간을 보내는 것조차 힘들었다. 저녁에는 맛있는 저녁을 먹지 못하고 혼자 시간을 보냈다."),
            Diary(3, "2024-06-26", "평범한 하루", "오늘은 평범한 하루였다. 아침에 일어나서 커피를 마시고, 오후에는 친구들과 시간을 보냈다. 저녁에는 맛있는 저녁을 먹고 가족과 함께 시간을 보냈다."),
        )))

        homeAdapter.setItemList(items)

        homeAdapter.setOnItemClickListener { item, _ ->
            when (item) {
                is HomeItem.HomeHeader -> return@setOnItemClickListener
                is HomeItem.HomeYear -> {

                }
                is HomeItem.HomeMonth -> {

                }
                is HomeItem.HomeDiary -> {
                    val diary = item.diaryList[0]
                    val intent = Intent(requireContext(), DiaryActivity::class.java)
                    intent.putExtra("date", diary.date)
                    intent.putExtra("title", diary.title)
                    intent.putExtra("content", diary.content)
                    startActivity(intent)
                }
            }
        }
    }
}