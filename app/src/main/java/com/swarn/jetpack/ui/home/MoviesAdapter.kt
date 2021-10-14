package com.swarn.jetpack.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.swarn.jetpack.R
import com.swarn.jetpack.databinding.ListItemMovieBinding
import com.swarn.jetpack.model.Movie
import com.swarn.jetpack.network.Config
import com.swarn.jetpack.util.Genre

class MoviesAdapter(private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val itemViewBinding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(movie: Movie) {
            itemView.setOnClickListener { view ->
                val action = HomeFragmentDirections.actionNavHomeToNavDetailTmdbFragment(movie.id)
                view.findNavController().navigate(action)
            }

            itemViewBinding.tvTitle.text = movie.title
            Glide.with(itemView).load(Config.TMDB_IMAGE_URL + movie.poster_path)
                .apply(
                    RequestOptions().override(400, 400).centerInside()
                        .placeholder(R.drawable.placehoder)
                ).into(itemViewBinding.ivPoster)
            itemViewBinding.tvGenre.text = Genre.getGenre(movie.genre_ids)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            ListItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Movie>) {
        list.clear()
        val sortedList = newList.sortedBy { movie -> movie.popularity }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }
}