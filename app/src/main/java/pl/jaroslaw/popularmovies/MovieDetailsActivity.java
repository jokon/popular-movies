package pl.jaroslaw.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;
import pl.jaroslaw.popularmovies.utilities.MovieDbApiHelper;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView movieTitle;
    ImageView moviePoster;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieTitle = (TextView) findViewById(R.id.detail_movie_title);
        moviePoster = (ImageView) findViewById(R.id.detail_movie_poster);
        Intent parentActivity = getIntent();

        if (parentActivity.hasExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT)) {
            Long movieId = parentActivity.getLongExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT, 0L);
            MovieDbService.getMovieDetails(this, movieId, getApiKey());
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        String movieImageUrl = MovieDbApiHelper.getUrlPosterFor(movie);
        Picasso.with(this).load(movieImageUrl).into(moviePoster);
        movieTitle.setText(movie.getTitle());
    }

    private String getApiKey() {
        return getResources().getString(R.string.api_key);
    }
}
