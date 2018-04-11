package pl.jaroslaw.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;
import pl.jaroslaw.popularmovies.utilities.MovieDbApiHelper;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView movieTitle;
    TextView movieDescription;
    TextView movieAvgVote;
    ImageView moviePoster;
    Movie movie;
    TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieTitle = (TextView) findViewById(R.id.detail_movie_title);
        moviePoster = (ImageView) findViewById(R.id.detail_movie_poster);
        movieAvgVote = (TextView) findViewById(R.id.detail_avg_vote);
        movieDescription = (TextView) findViewById(R.id.detail_desc);
        movieReleaseDate = (TextView) findViewById(R.id.detail_movie_release_date);
        Intent parentActivity = getIntent();

        if (parentActivity.hasExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT)) {
            Long movieId = parentActivity.getLongExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT, 0L);
            MovieDbService.getMovieDetails(this, movieId, getApiKey());
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        String movieImageUrl = MovieDbApiHelper.getUrlPosterFor(movie, MovieDbApiHelper.IMAGE_SIZE_DETAILED);
        Picasso.with(this).load(movieImageUrl).into(moviePoster);
        movieTitle.setText(movie.getTitle());
        movieAvgVote.setText(movie.getVoteAvarage() + "");
        movieDescription.setText(movie.getOverview());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        movieReleaseDate.setText("Release date: " + dt.format(movie.getReleaseDate()));
    }

    private String getApiKey() {
        return getResources().getString(R.string.api_key);
    }
}
