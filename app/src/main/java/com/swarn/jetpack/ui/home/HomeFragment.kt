package com.swarn.jetpack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.swarn.jetpack.databinding.FragmentHomeBinding
import com.swarn.jetpack.di.modules.TMDB_SCOPE
import com.swarn.jetpack.model.Movie
import com.swarn.jetpack.model.Result.*
import org.koin.android.ext.android.getKoin
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class HomeFragment : ScopeFragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()
    private val homeViewModel by viewModel<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private lateinit var moviesAdapter: MoviesAdapter
    private val list = ArrayList<Movie>()
    private var ourSession: Scope =
        getKoin().createScope(HomeFragment::class.java.name, named(TMDB_SCOPE))

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    init {
        scope.linkTo(ourSession)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        subscribeUi()

        return root
    }

    private fun init() {
        activity?.title = "Trending Movies"
        val layoutManager = LinearLayoutManager(activity)
        binding.rvMovies.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            binding.rvMovies.context,
            layoutManager.orientation
        )

        binding.rvMovies.addItemDecoration(dividerItemDecoration)
        moviesAdapter = MoviesAdapter(requireActivity(), list)
        binding.rvMovies.adapter = moviesAdapter
    }

    private fun subscribeUi() {
        homeViewModel.movieList.observe(requireActivity()) { result ->
            when (result) {
                is Loading -> {
                    binding.loading.visibility = View.VISIBLE
                }
                is Success -> {
                    result.data?.results?.let { list ->
                        moviesAdapter.updateData(list)
                    }
                    binding.loading.visibility = View.GONE
                }
                is Error -> {
                    result.message?.let {
                        showError(it)
                    }
                    binding.loading.visibility = View.GONE
                }
            }
        }
    }

    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.close()
        ourSession.close()
    }
}