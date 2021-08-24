package com.umbrella.mymovieskotlin.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.umbrella.mymovieskotlin.R
import com.umbrella.mymovieskotlin.databinding.FragmentFilmDetailBinding
import com.umbrella.mymovieskotlin.model.Film
import com.umbrella.mymovieskotlin.view.adapters.ReviewAdapter
import com.umbrella.mymovieskotlin.view.adapters.TrailerAdapter
import com.umbrella.mymovieskotlin.viewmodel.FilmDetailViewModel

private const val BIG_POSTER_URL = "https://image.tmdb.org/t/p/w780"

class FilmDetailFragment : Fragment() {

    private var _binding: FragmentFilmDetailBinding? = null
    private val binding get() = _binding!!
    private val reviewsAdapter = ReviewAdapter()
    private val trailersAdapter = TrailerAdapter()
    private val viewModel: FilmDetailViewModel by lazy {
        ViewModelProvider(this).get(FilmDetailViewModel::class.java)
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
        arguments?.let { bundle ->
            val film = bundle.getSerializable(FilmsFragment.ARG_FILM) as Film
            with(binding) {
                filmInfo.textViewTitle.text = film.title
                filmInfo.textViewOriginalTitle.text = film.originalTitle
                val posterUrl = BIG_POSTER_URL + film.posterPath
                Picasso.get()
                    .load(posterUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(bigPoster)
                filmInfo.textViewReleaseDate.text = film.releaseDate
                filmInfo.textViewRating.text = film.voteAverage.toString()
                filmInfo.textViewDescription.text = film.overview
            }

            trailersAdapter.setOnTrailerClickListener {
                val intentToTrailer = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intentToTrailer)
            }

            binding.filmInfo.recyclerViewReviews.adapter = reviewsAdapter
            binding.filmInfo.recyclerViewTrailers.adapter = trailersAdapter

            if (reviewsAdapter.getReviews().isEmpty() && trailersAdapter.getTrailers().isEmpty()) {
                downloadTrailersAndReviewsFromServerAndInitDownloadProgressObservers(film)
            }

            initAddToFavouriteButtonImageInitListenerAndButtonImageObserver(film)
        }
    }

    private fun initAddToFavouriteButtonImageInitListenerAndButtonImageObserver(film: Film) {
        val favouriteMovieLiveData = viewModel.getFavouriteMovieByIdFromDBLiveData(film.id)

        binding.imageViewAddToFavourite.setOnClickListener {
            if (favouriteMovieLiveData.value == null) {
                viewModel.insertFavouriteMovieIntoDB(film)
                Toast.makeText(context, "Фильм добавлен в избранное", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.deleteFavouriteMovieFromDB(film)
                Toast.makeText(context, "Фильм удален из избранного", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        favouriteMovieLiveData.observe(viewLifecycleOwner, { favouriteMovie ->
            if (favouriteMovie == null) {
                binding.imageViewAddToFavourite.setImageResource(android.R.drawable.btn_star_big_off)
            } else {
                binding.imageViewAddToFavourite.setImageResource(android.R.drawable.btn_star_big_on)
            }
        })
    }

    private fun downloadTrailersAndReviewsFromServerAndInitDownloadProgressObservers(film: Film) {
        viewModel.getReviewsLiveData().observe(viewLifecycleOwner, {
            reviewsAdapter.setReviews(it.reviews)
        })
        viewModel.getTrailersLiveData().observe(viewLifecycleOwner, {
            trailersAdapter.setTrailers(it.trailers)
        })
        viewModel.downloadReviews(film.id.toString())
        viewModel.downloadTrailers(film.id.toString())
    }
}