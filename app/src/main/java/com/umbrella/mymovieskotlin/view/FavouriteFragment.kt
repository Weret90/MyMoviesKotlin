package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.umbrella.mymovieskotlin.R
import com.umbrella.mymovieskotlin.databinding.FragmentFavouriteBinding
import com.umbrella.mymovieskotlin.view.adapters.FilmsAdapter
import com.umbrella.mymovieskotlin.viewmodel.FilmDetailViewModel

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmDetailViewModel by lazy {
        ViewModelProvider(this).get(FilmDetailViewModel::class.java)
    }
    private val favouriteFilmsAdapter = FilmsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favouriteFilmsRecyclerView.adapter = favouriteFilmsAdapter
        binding.favouriteFilmsRecyclerView.layoutManager = GridLayoutManager(context, getColumnCount())
        viewModel.getFavouriteFilmsFromDBLiveData().observe(viewLifecycleOwner, {
            favouriteFilmsAdapter.setMovies(it)
        })
        favouriteFilmsAdapter.setOnFilmClickListener {
            val bundle = Bundle()
            bundle.putSerializable(FilmsFragment.ARG_FILM, it)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
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
}