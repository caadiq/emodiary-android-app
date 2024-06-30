package com.toy.project.emodiary.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.toy.project.emodiary.view.adapter.HomeItem

class HomeDiffUtil(private val oldList: List<HomeItem>, private val newList: List<HomeItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]::class == newList[newItemPosition]::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}