package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.umbrella.mymovieskotlin.R
import com.umbrella.mymovieskotlin.databinding.FragmentFilmsBinding
import com.umbrella.mymovieskotlin.model.AppState
import com.umbrella.mymovieskotlin.view.adapters.FilmsAdapter
import com.umbrella.mymovieskotlin.viewmodel.MainViewModel

private const val SORT_BY_POPULARITY = "popularity.desc"
private const val SORT_BY_RATING = "vote_count.desc"

class FilmsFragment : Fragment() {

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val filmsAdapter = FilmsAdapter()
    private var switchButtonIsChecked = false
    private var page = 1
    private var isError = false

    companion object {
        //        const val HORROR = "27"
//        const val COMEDY = "35"
//        const val ACTION = "28"
        const val ARG_FILM = "film"
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

        binding.recyclerViewFilms.adapter = filmsAdapter

        binding.switchSort.isChecked = switchButtonIsChecked
        checkSwitchSortTextViewColors()

        initFilmsFirstPageObserver()

        initFilmsNextPagesObserver()

        if (filmsAdapter.getFilms().isEmpty()) {
            page = 1
            viewModel.downloadFilmsFromServer(SORT_BY_POPULARITY, page)
        }

        initListeners()
    }

    private fun initListeners() {
        filmsAdapter.setOnFilmClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ARG_FILM, it)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
        }

        filmsAdapter.setOnReachEndListener {
            if (switchButtonIsChecked) {
                viewModel.downloadFilmsFromServer(SORT_BY_RATING, page)
            } else {
                viewModel.downloadFilmsFromServer(SORT_BY_POPULARITY, page)
            }
        }

        binding.switchSort.setOnCheckedChangeListener { _, isChecked ->
            page = 1
            isError = false
            switchButtonIsChecked = if (isChecked) {
                viewModel.downloadFilmsFromServer(SORT_BY_RATING, page)
                true
            } else {
                viewModel.downloadFilmsFromServer(SORT_BY_POPULARITY, page)
                false
            }
            checkSwitchSortTextViewColors()
        }
    }

    private fun checkSwitchSortTextViewColors() {
        if (switchButtonIsChecked) {
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.purple_500))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.white))
        } else {
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.white))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.purple_500))
        }
    }

    private fun initFilmsNextPagesObserver() {
        viewModel.getFilmsFromServerNextPagesLiveData().observe(viewLifecycleOwner, { result ->
            when (result) {
                is AppState.Success -> {
                    isError = false
                    page++
                    filmsAdapter.addMovies(result.response.films)
                }
                is AppState.Error -> {
                    if (!isError) {
                        isError = true
                        Toast.makeText(
                            context,
                            "ОШИБКА: " + result.throwable.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun initFilmsFirstPageObserver() {
        viewModel.getFilmsFromServerFirstPageLiveData().observe(viewLifecycleOwner, { result ->
            page = 2
            when (result) {
                is AppState.Loading -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    isError = false
                    if (switchButtonIsChecked) {
                        viewModel.clearAllMoviesInRatingFilmsDBAndInsertFreshData(result.response.films)
                    } else {
                        viewModel.clearAllMoviesInPopularityFilmsDBAndInsertFreshData(result.response.films)
                    }
                    filmsAdapter.setMovies(result.response.films)
                    binding.loadingLayout.visibility = View.GONE
                }
                is AppState.Error -> {
                    binding.loadingLayout.visibility = View.GONE
                    if (switchButtonIsChecked) {
                        viewModel.getRatingFilmsFromDBLiveData()
                            .observe(viewLifecycleOwner, { ratingFilms ->
                                filmsAdapter.setMovies(ratingFilms)
                            })
                    } else {
                        viewModel.getPopularityFilmsFromDBLiveData()
                            .observe(viewLifecycleOwner, { popularityFilms ->
                                filmsAdapter.setMovies(popularityFilms)
                            })
                    }
                    if (!isError) {
                        isError = true
                        Toast.makeText(
                            context,
                            "ОШИБКА: " + result.throwable.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(
                            context,
                            "Отображены фильмы из базы данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
}