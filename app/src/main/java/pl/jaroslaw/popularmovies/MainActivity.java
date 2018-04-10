package pl.jaroslaw.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;
import pl.jaroslaw.popularmovies.utilities.EndlessRecyclerViewScrollListener;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private int SPAN_COUNT = 2;
    private MovieAdapter movieAdapter;
    private RecyclerView moviesView;
    private EndlessRecyclerViewScrollListener scrollListener;
    public static final String MOVIE_ID_TAG_PASSED_BY_INTENT = "movieId";
    private MovieDbService.MoviesOrder moviesOrder = MovieDbService.MoviesOrder.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Home");
        setContentView(R.layout.activity_main);

        moviesView = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
        moviesView.setLayoutManager(gridLayoutManager);
        moviesView.setHasFixedSize(false);
        movieAdapter = new MovieAdapter(this);
        moviesView.setAdapter(movieAdapter);

        moviesView.setVisibility(View.VISIBLE);
        
        MovieDbService.listMovies(movieAdapter, 1, moviesOrder, getApiKey());

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        moviesView.addOnScrollListener(scrollListener);
    }

    public void loadNextDataFromApi(int offset) {
        MovieDbService.appendNextPage(movieAdapter, offset, moviesOrder, getApiKey());
    }

    @Override
    public void onItemClick(int clickedIndex) {
        Movie movie = movieAdapter.getMovies().get(clickedIndex);
        Intent movieDetailsActivityIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsActivityIntent.putExtra(MOVIE_ID_TAG_PASSED_BY_INTENT, movie.getId());
        startActivity(movieDetailsActivityIntent);
        Log.d(MainActivity.class.getSimpleName(), movie.getTitle());
    }

    private String getApiKey() {
        return getResources().getString(R.string.api_key);
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
            MovieDbService.listMovies(movieAdapter, 1, moviesOrder, getApiKey());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
