package pl.jaroslaw.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.utilities.MovieDbApiHelper;

/**
 * Created by jokonski on 26.03.18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    final private ItemClickListener itemClickListener;
    private Context context;

    private int moviesCount;

    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(@NonNull ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void appendMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void clear() {
        int currentMoviesSize = movies.size();
        movies.clear();
        notifyDataSetChanged();
        //notifyItemRangeRemoved(0, currentMoviesSize);

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

        //movieViewHolder.tvMovieTitle = parent.findViewById(R.id.tv_movie_title);
        //movieViewHolder.tvMovieTitle.setText("Movie TEST 1");

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvMovieTitle.setText(movie.getTitle());
        String movieImageUrl = MovieDbApiHelper.getUrlPosterFor(movie);
        Picasso.with(context).load(movieImageUrl).into(holder.ivMovieImage);
    }

    @Override
    public int getItemCount() {
        return movies == null ?  0 : movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        //@BindView(R.id.tv_movie_title)
        public TextView tvMovieTitle;
        public ImageView ivMovieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            ivMovieImage = itemView.findViewById(R.id.ivMovieImage);
        }

        void bind(int listIndex) {
            tvMovieTitle.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            itemClickListener.onItemClick(clickedPosition);
        }
    }
}
