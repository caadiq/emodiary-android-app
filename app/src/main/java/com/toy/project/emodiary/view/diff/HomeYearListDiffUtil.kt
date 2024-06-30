package com.toy.project.emodiary.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.toy.project.emodiary.view.adapter.Year

class HomeYearListDiffUtil(private val oldList: List<Year>, private val newList: List<Year>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].year == newList[newItemPosition].year
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}