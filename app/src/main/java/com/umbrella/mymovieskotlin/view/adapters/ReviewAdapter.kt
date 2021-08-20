package com.umbrella.mymovieskotlin.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umbrella.mymovieskotlin.databinding.ReviewItemBinding
import com.umbrella.mymovieskotlin.model.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: List<Review> = ArrayList()

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    fun getReviews() = reviews

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ReviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    inner class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.textViewAuthor.text = review.author
            binding.textViewContent.text = review.content
        }
    }
}