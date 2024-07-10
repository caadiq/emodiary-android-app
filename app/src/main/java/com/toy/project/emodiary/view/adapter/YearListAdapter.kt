package com.toy.project.emodiary.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.RowHomeYearBinding
import com.toy.project.emodiary.view.diff.YearListDiffUtil

data class Year(
    val year: Int,
    val count: Int,
    var selected: Boolean
)

class YearListAdapter : RecyclerView.Adapter<YearListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Year>()
    private var onItemClickListener: ((Year, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeYearBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeYearBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Year) {
            binding.txtYearFull.text = item.year.toString()
            binding.txtYearAbbr.text = item.year.toString().substring(2)
            binding.txtCount.text = "${item.count}개의 기억들"
            if (item.selected) {
                binding.layoutParent.apply {
                    setBackgroundResource(R.drawable.rectangle_blue_rounded_16dp)
                    elevation = 16f
                }
                binding.txtYearFull.setTextColor(getColor(binding.root.context.resources, R.color.white, null))
                binding.txtYearAbbr.setTextColor(Color.parseColor("#5084E9"))
                binding.txtCount.setTextColor(getColor(binding.root.context.resources, R.color.white, null))
            } else {
                binding.layoutParent.apply {
                    setBackgroundResource(R.drawable.rectangle_light_blue_rounded_16dp)
                    elevation = 0f
                }
                binding.txtYearFull.setTextColor(Color.parseColor("#739DF0"))
                binding.txtYearAbbr.setTextColor(Color.parseColor("#AFCAFF"))
                binding.txtCount.setTextColor(Color.parseColor("#739DF0"))
            }
        }
    }

    fun setOnItemClickListener(listener: (Year, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Year>) {
        val diffCallback = YearListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItemSelect(position: Int) {
        val previousItemSelected = itemList.indexOfFirst { it.selected }
        if (previousItemSelected != -1) {
            itemList[previousItemSelected].selected = false
            notifyItemChanged(previousItemSelected)
        }
        itemList[position].selected = true
        notifyItemChanged(position)
    }
}