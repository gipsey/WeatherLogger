package edu.davidd.weatherlogger.framework.ui.xml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.davidd.weatherlogger.databinding.LayoutWeatherItemBinding
import edu.davidd.weatherlogger.framework.ui.model.WeatherItem

class WeatherItemsAdapter :
    ListAdapter<WeatherItem, WeatherItemsAdapter.WeatherViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherViewHolder(LayoutWeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun setData(data: List<WeatherItem>, callback: () -> Unit) = submitList(data, callback)

    class WeatherViewHolder(private val binding: LayoutWeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherItem) {
            binding.temperatureTextView.text = item.temperature
            binding.dateTextView.text = item.date
            binding.locationTextView.text = item.location
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<WeatherItem>() {
        override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem) = oldItem.date == newItem.date
        override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem) = oldItem == newItem
    }
}