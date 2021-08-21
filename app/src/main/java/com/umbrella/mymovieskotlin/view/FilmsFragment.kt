package com.umbrella.mymovieskotlin.view

import android.os.Bundle
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
    private var isDownloading = false

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
        checkSwitchSortTextViewsColors()

        initMainObserver()

        initListeners()

        if (filmsAdapter.getFilms().isEmpty()) {
            page = 1
            setFilmsFromDB()
            viewModel.downloadFilmsFromServer(SORT_BY_POPULARITY, page)
        }
    }

    private fun initListeners() {
        filmsAdapter.setOnFilmClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ARG_FILM, it)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
        }

        filmsAdapter.setOnReachEndListener {
            if (!isDownloading) {
                downloadDataWithSortCheck()
            }
        }

        binding.switchSort.setOnCheckedChangeListener { _, isChecked ->
            page = 1
            switchButtonIsChecked = isChecked
            setFilmsFromDB()
            downloadDataWithSortCheck()
            checkSwitchSortTextViewsColors()
        }
    }

    private fun downloadDataWithSortCheck() {
        if (switchButtonIsChecked) {
            viewModel.downloadFilmsFromServer(SORT_BY_RATING, page)
        } else {
            viewModel.downloadFilmsFromServer(SORT_BY_POPULARITY, page)
        }
    }

    private fun checkSwitchSortTextViewsColors() {
        if (switchButtonIsChecked) {
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.purple_500))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.white))
        } else {
            binding.textViewTopRated.setTextColor(resources.getColor(R.color.white))
            binding.textViewMostPopular.setTextColor(resources.getColor(R.color.purple_500))
        }
    }

    private fun setFilmsFromDB() {
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
    }

    private fun initMainObserver() {
        viewModel.getFilmsFromServerLiveData().observe(viewLifecycleOwner, { result ->
            when (result) {
                is AppState.Loading -> {
                    isDownloading = true
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                is AppState.Success -> {
                    if (page == 1) {
                        if (switchButtonIsChecked) {
                            viewModel.clearAllMoviesInRatingFilmsDBAndInsertFreshData(result.response.films)
                        } else {
                            viewModel.clearAllMoviesInPopularityFilmsDBAndInsertFreshData(result.response.films)
                        }
                        filmsAdapter.clear()
                    }
                    filmsAdapter.addMovies(result.response.films)
                    binding.loadingLayout.visibility = View.GONE
                    page++
                    isDownloading = false
                }
                is AppState.Error -> {
                    binding.loadingLayout.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "ОШИБКА: " + result.throwable.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    isDownloading = false
                }
            }
        })
    }
}