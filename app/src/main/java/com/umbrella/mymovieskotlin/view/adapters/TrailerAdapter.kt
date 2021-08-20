package com.umbrella.mymovieskotlin.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.mymovieskotlin.databinding.TrailerItemBinding
import com.umbrella.mymovieskotlin.model.Trailer

class TrailerAdapter : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    private var trailers: List<Trailer> = ArrayList()
    private var onTrailerClickListener: (url: String) -> Unit = {}
    private val url = "https://www.youtube.com/watch?v="

    fun setTrailers(trailers: List<Trailer>) {
        this.trailers = trailers
        notifyDataSetChanged()
    }

    fun getTrailers() = trailers

    fun setOnTrailerClickListener(onTrailerClickListener: (url: String) -> Unit) {
        this.onTrailerClickListener = onTrailerClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding = TrailerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bind(trailers[position])
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    inner class TrailerViewHolder(private val binding: TrailerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trailer: Trailer) {
            binding.textViewNameOfVideo.text = trailer.name

            binding.textViewNameOfVideo.setOnClickListener {
                onTrailerClickListener(url + trailer.key)
            }
        }
    }
}