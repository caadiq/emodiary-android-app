package com.toy.project.emodiary.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.RowHomeDiaryBinding
import com.toy.project.emodiary.view.diff.HomeDiaryListDiffUtil
import com.toy.project.emodiary.view.utils.DateTimeConverter.stringToDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Diary(
    val diaryId: Int,
    val date: String,
    val title: String,
    val content: String
)

class HomeDiaryListAdapter : RecyclerView.Adapter<HomeDiaryListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<Diary>()
    private var onItemClickListener: ((Diary, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeDiaryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeDiaryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: Diary) {
            val localDate = stringToDate(item.date, "yyyy-MM-dd", Locale.KOREA)
            binding.txtDate.text = localDate.format(DateTimeFormatter.ofPattern("dd", Locale.KOREA))
            binding.txtDay.text = localDate.format(DateTimeFormatter.ofPattern("EEEE", Locale.KOREA))
            binding.txtTitle.text = item.title
            binding.txtContent.text = item.content
            binding.imgMood.setImageResource(R.drawable.icon_mood_happy)
            binding.imgWeather.setImageResource(R.drawable.icon_weather_sunny)
        }
    }

    fun setOnItemClickListener(listener: (Diary, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<Diary>) {
        val diffCallback = HomeDiaryListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}