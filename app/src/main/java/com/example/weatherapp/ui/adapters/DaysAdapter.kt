package com.example.weatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ListItemBinding
import com.example.weatherapp.models.ForecastDay
import com.squareup.picasso.Picasso

class DaysAdapter(
    private val context: Context
) : ListAdapter<ForecastDay, DaysAdapter.DaysViewHolder>(DiffCallback) {

    inner class DaysViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastDay) = with(binding) {
            date.text = item.date
            condition.text = item.day.condition.text
            val minTemp = item.day.minTemp.toInt().toString()
            val maxTemp = item.day.maxTemp.toInt().toString()
            temp.text = context.getString(R.string.temp_min_max, minTemp, maxTemp)
            temp.textSize = 20f
            Picasso.get().load("https:" + item.day.condition.icon).into(imageView)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ForecastDay>() {
        override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return DaysViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}