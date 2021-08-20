package com.umbrella.mymovieskotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    private val adapter = FilmsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favouriteFilmsRecyclerView.adapter = adapter
        viewModel.getFavouriteFilmsFromDBLiveData().observe(viewLifecycleOwner, {
            adapter.setMovies(it)
        })
        adapter.setOnFilmClickListener {
            val bundle = Bundle()
            bundle.putSerializable(FilmsFragment.ARG_FILM, it)
            findNavController().navigate(R.id.filmDetailFragment, bundle)
        }
    }
}