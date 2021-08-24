package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
    private var isError = false

    companion object {
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
        binding.recyclerViewFilms.layoutManager = GridLayoutManager(context, getColumnCount())

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
            isError = false
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

    private fun getColumnCount(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
        val width = displayMetrics.widthPixels / displayMetrics.density
        val columnCount = (width / 185).toInt()
        return if (columnCount > 2) {
            columnCount
        } else {
            2
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
                    isError = false
                    isDownloading = false
                }
                is AppState.Error -> {
                    if (!isError) {
                        Toast.makeText(
                            context,
                            getString(R.string.error) + result.throwable.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding.loadingLayout.visibility = View.GONE
                    isError = true
                    isDownloading = false
                }
            }
        })
    }
}