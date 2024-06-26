package com.toy.project.emodiary.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.RowHomeMonthBinding
import com.toy.project.emodiary.view.diff.HomeMonthListDiffUtil

data class Month(
    val month: Int,
    var selected: Boolean
)

class HomeMonthListAdapter : RecyclerView.Adapter<HomeMonthListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Month>()
    private var onItemClickListener: ((Month, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeMonthBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Month) {
            binding.txtMonth.text = "${item.month}월"
            if (item.selected) {
                binding.layoutParent.apply {
                    setBackgroundResource(R.drawable.rectangle_blue2_rounded_16dp)
                    elevation = 16f
                }
                binding.txtMonth.setTextColor(getColor(binding.root.context.resources, R.color.white, null))
            } else {
                binding.layoutParent.apply {
                    setBackgroundResource(R.drawable.rectangle_light_blue2_rounded_16dp)
                    elevation = 0f
                }
                binding.txtMonth.setTextColor(Color.parseColor("#9AB7F3"))
            }
        }
    }

    fun setOnItemClickListener(listener: (Month, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Month>) {
        val diffCallback = HomeMonthListDiffUtil(itemList, list)
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