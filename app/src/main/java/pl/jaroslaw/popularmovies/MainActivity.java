package pl.jaroslaw.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;
import pl.jaroslaw.popularmovies.utilities.EndlessRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private static final int LANDSCAPE_SPAN_COUNT = 4;
    private static final int PORTRAIT_SPAN_COUNT = 2;
    public static final String MOVIE_ID_TAG_PASSED_BY_INTENT = "movieId";

    @BindView(R.id.rv_movies)
    public RecyclerView moviesView;

    private MovieAdapter movieAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private MovieDbService.MoviesOrder moviesOrder = MovieDbService.MoviesOrder.TOP_RATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, PORTRAIT_SPAN_COUNT);
        moviesView.setLayoutManager(gridLayoutManager);
        moviesView.setHasFixedSize(false);
        movieAdapter = new MovieAdapter(this);
        moviesView.setAdapter(movieAdapter);

        moviesView.setVisibility(View.VISIBLE);

        MovieDbService.listMovies(movieAdapter, 1, moviesOrder);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        moviesView.addOnScrollListener(scrollListener);

        if(this.moviesView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager.setSpanCount(PORTRAIT_SPAN_COUNT);
            moviesView.setLayoutManager(gridLayoutManager);
        } else if (this.moviesView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(LANDSCAPE_SPAN_COUNT);
            moviesView.setLayoutManager(gridLayoutManager);
        }

    }

    private void updateTitle() {
        setTitle(getString(moviesOrder.getResourceTitleId()));
    }

    public void loadNextDataFromApi(int offset) {
        MovieDbService.appendNextPage(movieAdapter, offset, moviesOrder);
    }

    @Override
    public void onItemClick(int clickedIndex) {
        Movie clickedMovie = movieAdapter.getMovies().get(clickedIndex);
        Log.d(MainActivity.class.getSimpleName(), clickedMovie.getTitle());

        Intent movieDetailsActivityIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsActivityIntent.putExtra(MOVIE_ID_TAG_PASSED_BY_INTENT, clickedMovie.getId());

        startActivity(movieDetailsActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        boolean ifOrderWasChanged = false;
        if (itemId == R.id.order_most_popular) {
            if (! moviesOrder.equals(MovieDbService.MoviesOrder.MOST_POPULAR)) {
                moviesOrder = MovieDbService.MoviesOrder.MOST_POPULAR;
                ifOrderWasChanged = true;
            } else {
                return true;
            }
        } else if (itemId == R.id.order_top_rated) {
            if (! moviesOrder.equals(MovieDbService.MoviesOrder.TOP_RATED)) {
                moviesOrder = MovieDbService.MoviesOrder.TOP_RATED;
                ifOrderWasChanged = true;
            } else {
                return true;
            }
        }

        if (ifOrderWasChanged) {
            movieAdapter.clear();
            MovieDbService.listMovies(movieAdapter, 1, moviesOrder);
            updateTitle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
