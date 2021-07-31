package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.umbrella.mymovieskotlin.databinding.FragmentFilmDetailBinding
import com.umbrella.mymovieskotlin.model.Film

class FilmDetailFragment : Fragment() {

    private var _binding: FragmentFilmDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val POSTER_URL = "https://image.tmdb.org/t/p/original"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val film = it.getSerializable(FilmsFragment.ARG_FILM) as Film
            binding.filmDescriptionTitle.text = film.title
            val posterUrl = POSTER_URL + film.posterPath
            Picasso.get()
                .load(posterUrl)
                .into(binding.filmDescriptionPoster)
            binding.filmDescriptionYear.text = film.releaseDate
            binding.filmDescriptionRating.text = film.voteAverage.toString()
            binding.filmDescriptionDescription.text = film.overview
        }
    }
}