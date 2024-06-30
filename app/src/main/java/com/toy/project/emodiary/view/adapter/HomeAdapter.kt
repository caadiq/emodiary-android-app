package com.toy.project.emodiary.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toy.project.emodiary.databinding.RowHomeDiaryListBinding
import com.toy.project.emodiary.databinding.RowHomeHeaderBinding
import com.toy.project.emodiary.databinding.RowHomeTabMonthBinding
import com.toy.project.emodiary.databinding.RowHomeTabYearBinding
import com.toy.project.emodiary.view.diff.HomeDiffUtil
import java.util.Calendar

sealed class HomeItem {
    data class HomeHeader(val greeting: String, val name: String) : HomeItem()
    data class HomeYear(val yearList: List<Year>) : HomeItem()
    data class HomeMonth(val monthList: List<Month>) : HomeItem()
    data class HomeDiary(val diaryList: List<Diary>) : HomeItem()
}

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<HomeItem>()
    private var onItemClickListener: ((HomeItem, Int) -> Unit)? = null
    private val viewPool = RecyclerView.RecycledViewPool()

    private val currentMonthPosition = Calendar.getInstance().get(Calendar.MONTH)

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is HomeItem.HomeHeader -> 0
            is HomeItem.HomeYear -> 1
            is HomeItem.HomeMonth -> 2
            is HomeItem.HomeDiary -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val binding = RowHomeHeaderBinding.inflate(inflater, parent, false)
                HeaderViewHolder(binding)
            }
            1 -> {
                val binding = RowHomeTabYearBinding.inflate(inflater, parent, false)
                YearViewHolder(binding)
            }
            2 -> {
                val binding = RowHomeTabMonthBinding.inflate(inflater, parent, false)
                MonthViewHolder(binding)
            }
            3 -> {
                val binding = RowHomeDiaryListBinding.inflate(inflater, parent, false)
                DiaryViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is HomeItem.HomeHeader -> (holder as HeaderViewHolder).bind(item)
            is HomeItem.HomeYear -> (holder as YearViewHolder).bind(item)
            is HomeItem.HomeMonth -> (holder as MonthViewHolder).bind(item)
            is HomeItem.HomeDiary -> (holder as DiaryViewHolder).bind(item)
        }
    }

    inner class HeaderViewHolder(private val binding: RowHomeHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeItem.HomeHeader) {
            binding.txtGreeting.text = item.greeting
            binding.txtName.text = item.name
        }
    }

    inner class YearViewHolder(binding: RowHomeTabYearBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeYearListAdapter = HomeYearListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeYearListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }

            homeYearListAdapter.setOnItemClickListener { item, position ->
                val parentPosition = adapterPosition
                if (parentPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(HomeItem.HomeYear(listOf(item)), parentPosition)
                    homeYearListAdapter.setItemSelect(position)
                }
            }
        }

        fun bind(item: HomeItem.HomeYear) {
            homeYearListAdapter.setItemList(item.yearList)
        }
    }

    inner class MonthViewHolder(binding: RowHomeTabMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeMonthListAdapter = HomeMonthListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeMonthListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
                scrollToPosition(currentMonthPosition)
            }

            homeMonthListAdapter.setOnItemClickListener { item, position ->
                val parentPosition = adapterPosition
                if (parentPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(HomeItem.HomeMonth(listOf(item)), parentPosition)
                    homeMonthListAdapter.setItemSelect(position)
                }
            }
        }

        fun bind(item: HomeItem.HomeMonth) {
            homeMonthListAdapter.setItemList(item.monthList)
        }
    }

    inner class DiaryViewHolder(binding: RowHomeDiaryListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeDiaryListAdapter = HomeDiaryListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeDiaryListAdapter
                itemAnimator = null
                setRecycledViewPool(viewPool)
                setHasFixedSize(true)
            }

            homeDiaryListAdapter.setOnItemClickListener { item, _ ->
                val parentPosition = adapterPosition
                if (parentPosition != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(HomeItem.HomeDiary(listOf(item)), parentPosition)
                }
            }
        }

        fun bind(item: HomeItem.HomeDiary) {
            homeDiaryListAdapter.setItemList(item.diaryList)
        }
    }

    fun setOnItemClickListener(listener: (HomeItem, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<HomeItem>) {
        val diffCallback = HomeDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}