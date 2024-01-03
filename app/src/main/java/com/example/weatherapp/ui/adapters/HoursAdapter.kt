package com.example.weatherapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ListItemBinding
import com.example.weatherapp.models.Hour
import com.squareup.picasso.Picasso

class HoursAdapter(private val context: Context) :
    ListAdapter<Hour, HoursAdapter.HoursViewHolder>(DiffCallback) {

    inner class HoursViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Hour) = with(binding) {
            date.text = item.time.substring(11)
            condition.text = item.condition.text
            temp.text = context.getString(R.string.current_temp, item.temp.toString())
            Picasso.get().load("https:" + item.condition.icon).into(imageView)
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return HoursViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}