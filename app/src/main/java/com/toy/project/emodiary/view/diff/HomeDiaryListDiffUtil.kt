package com.toy.project.emodiary.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.toy.project.emodiary.view.adapter.Diary

class HomeDiaryListDiffUtil(private val oldList: List<Diary>, private val newList: List<Diary>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].diaryId == newList[newItemPosition].diaryId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}