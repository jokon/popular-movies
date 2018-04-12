package pl.jaroslaw.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;

/**
 * Created by jokonski on 26.03.18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    final private ItemClickListener itemClickListener;
    private Context context;

    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void appendMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void clear() {
        movies.clear();
        notifyDataSetChanged();

    }

    public interface ItemClickListener {
        void onItemClick(int clickedIndex);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder movieViewHolder= new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvMovieTitle.setText(movie.getTitle());
        holder.tvAvgVote.setText(movie.getVoteAvarage() + "");
        String movieImageUrl = MovieDbService.getUrlPosterFor(movie);
        Picasso.with(context).load(movieImageUrl).into(holder.ivMovieImage);
    }

    @Override
    public int getItemCount() {
        return movies == null ?  0 : movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.tv_movie_title)
        public TextView tvMovieTitle;

        @BindView(R.id.ivMovieImage)
        public ImageView ivMovieImage;

        @BindView(R.id.tv_movie_vote)
        public TextView tvAvgVote;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            itemClickListener.onItemClick(clickedPosition);
        }
    }
}
