package com.umbrella.mymovieskotlin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.umbrella.mymovieskotlin.databinding.ItemFilmBinding
import com.umbrella.mymovieskotlin.model.Film

class FilmsAdapter : RecyclerView.Adapter<FilmsAdapter.MyViewHolder>() {

    private var films: MutableList<Film> = mutableListOf()
    private var onFilmClickListener: OnFilmClickListener? = null

    companion object {
        private const val SMALL_POSTER_URL = "https://image.tmdb.org/t/p/w185"
        private const val BIG_POSTER_URL = "https://image.tmdb.org/t/p/w780"
    }

    fun setMovies(films: List<Film>) {
        this.films = films as MutableList<Film>
        notifyDataSetChanged()
    }

    fun addMovies(films: List<Film>) {
        this.films.addAll(films)
        notifyDataSetChanged()
    }

    fun getFilms(): List<Film> {
        return films
    }

    interface OnFilmClickListener {
        fun onFilmClick(position: Int)
    }

    fun setOnFilmClickListener(onFilmClickListener: OnFilmClickListener) {
        this.onFilmClickListener = onFilmClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val film = films[position]
        holder.bind(film)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    inner class MyViewHolder(private val binding: ItemFilmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(film: Film) {
            val smallPosterFullUrl = SMALL_POSTER_URL + film.posterPath
            Picasso.get()
                .load(smallPosterFullUrl)
                .into(binding.imageViewSmallPoster)
            binding.root.setOnClickListener {
                onFilmClickListener?.let {
                    it.onFilmClick(adapterPosition)
                }
            }
        }

    }
}