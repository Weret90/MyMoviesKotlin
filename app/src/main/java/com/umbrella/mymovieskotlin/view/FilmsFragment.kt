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

    companion object {
        const val HORROR = "27"
        const val COMEDY = "35"
        const val ACTION = "28"
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
        loadingBarLaunch()
        binding.recyclerViewHorrors.adapter = makeAdapter(HORROR)
        binding.recyclerViewComedies.adapter = makeAdapter(COMEDY)
        binding.recyclerViewActions.adapter = makeAdapter(ACTION)
        viewModel.makeApiCall()
    }

    private fun makeAdapter(genre: String): FilmsAdapter {
        val adapter = FilmsAdapter()
        viewModel.getData(genre).observe(viewLifecycleOwner, {
            adapter.upgradeFilms(it.films)
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

    private fun loadingBarLaunch() {
        binding.loadingLayout.visibility = View.VISIBLE
        viewModel.getLoadingBarLiveData().observe(viewLifecycleOwner, {
            binding.loadingLayout.visibility = View.GONE
            binding.labelActions.visibility = View.VISIBLE
            binding.labelHorrors.visibility = View.VISIBLE
            binding.labelComedies.visibility = View.VISIBLE
        })
    }
}