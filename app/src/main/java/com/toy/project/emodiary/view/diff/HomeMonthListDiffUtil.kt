package com.toy.project.emodiary.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.toy.project.emodiary.view.adapter.Month

class HomeMonthListDiffUtil(private val oldList: List<Month>, private val newList: List<Month>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].month == newList[newItemPosition].month
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}