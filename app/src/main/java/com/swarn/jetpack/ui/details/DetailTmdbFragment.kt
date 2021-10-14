package com.swarn.jetpack.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.swarn.jetpack.R
import com.swarn.jetpack.databinding.DetailTmdbFragmentBinding
import com.swarn.jetpack.di.modules.TMDB_SCOPE
import com.swarn.jetpack.model.MovieDesc
import com.swarn.jetpack.model.Result.*
import com.swarn.jetpack.network.Config
import org.koin.android.ext.android.getKoin
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class DetailTmdbFragment : ScopeFragment(), AndroidScopeComponent {

    override val scope: Scope by fragmentScope()
    private val detailTmdbViewModel by viewModel<DetailTmdbViewModel>()
    private var _binding: DetailTmdbFragmentBinding? = null
    private var ourSession: Scope =
        getKoin().createScope(DetailTmdbFragment::class.java.name, named(TMDB_SCOPE))

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding
        get() = _binding!!

    init {
        scope.linkTo(ourSession)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailTmdbFragmentBinding.inflate(inflater, container, false)

        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {
        val args: DetailTmdbFragmentArgs by navArgs()

        args.tmdbMovieId.let { movieId ->
            detailTmdbViewModel.getMovieDetail(movieId).observe(requireActivity()) { result ->
                when (result) {
                    is Loading -> {
                        result.data?.let { movieDesc ->
                            updateUi(movieDesc)
                        }
                        binding.loading.visibility = View.VISIBLE
                    }
                    is Success -> {
                        result.data?.let { movieDesc ->
                            updateUi(movieDesc)
                        }
                        binding.loading.visibility = View.GONE
                    }
                    is Error -> {
                        result.message?.let { errorMsg ->
                            showError(errorMsg)
                        }
                        binding.loading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun updateUi(movie: MovieDesc) {
        activity?.title = movie.title
        binding.tvTitle.text = movie.title
        binding.tvDescription.text = movie.overview
        Glide.with(this).load(Config.TMDB_IMAGE_URL + movie.poster_path)
            .apply(
                RequestOptions().override(400, 400).centerInside()
                    .placeholder(R.drawable.placehoder)
            ).into(binding.ivCover)

        val genreNames = mutableListOf<String>()
        movie.genres.map {
            genreNames.add(it.name)
        }
        binding.tvGenre.text = genreNames.joinToString(separator = ", ")
    }

    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        ourSession.close()
        scope.close()
    }
}