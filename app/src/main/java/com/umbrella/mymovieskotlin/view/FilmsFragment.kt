package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.mymovieskotlin.R
import com.umbrella.mymovieskotlin.view.adapter.FilmsAdapter
import com.umbrella.mymovieskotlin.databinding.FragmentFilmsBinding
import com.umbrella.mymovieskotlin.viewmodel.MainViewModel

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private var isRatingFilms = false

    companion object {
        //        const val HORROR = "27"
//        const val COMEDY = "35"
//        const val ACTION = "28"
        const val ARG_FILM = "film"
        const val SORT_BY_POPULARITY = "popularity.desc"
        const val SORT_BY_RATING = "vote_count.desc"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingLayout.visibility = View.VISIBLE

        binding.recyclerViewFilms.adapter = makeAdapter()

        checkSortMethod()

        binding.switchSort.setOnClickListener {
            isRatingFilms = binding.switchSort.isChecked
            checkSortMethod()
        }

    }

    private fun makeAdapter(): FilmsAdapter {
        val adapter = FilmsAdapter()
        viewModel.getData().observe(viewLifecycleOwner, {
            adapter.setMovies(it.films)
            binding.loadingLayout.visibility = View.GONE
        })
        adapter.setOnFilmClickListener(object : FilmsAdapter.OnFilmClickListener {
            override fun onFilmClick(position: Int) {
                val film = adapter.getFilms()[position]
                val bundle = Bundle()
                bundle.putSerializable(ARG_FILM, film)
                findNavController().navigate(R.id.filmDetailFragment, bundle)
            }
        })
        return adapter
    }

    private fun checkSortMethod() {
        if (isRatingFilms) {
            binding.loadingLayout.visibility = View.VISIBLE
            viewModel.makeApiCall(SORT_BY_RATING)
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.purple_500))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.white))

        } else {
            binding.loadingLayout.visibility = View.VISIBLE
            viewModel.makeApiCall(SORT_BY_POPULARITY)
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.white))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.purple_500))
        }
    }
}