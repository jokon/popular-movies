package pl.jaroslaw.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.service.MovieDbService;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.detail_movie_title)
    TextView movieTitle;

    @BindView(R.id.detail_desc)
    TextView movieDescription;

    @BindView(R.id.detail_avg_vote)
    TextView movieAvgVote;

    @BindView(R.id.detail_movie_poster)
    ImageView moviePoster;
    Movie movie;

    @BindView(R.id.detail_movie_release_date)
    TextView movieReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.movie_detail_title));
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent parentActivity = getIntent();

        if (parentActivity.hasExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT)) {
            Long movieId = parentActivity.getLongExtra(MainActivity.MOVIE_ID_TAG_PASSED_BY_INTENT, 0L);
            MovieDbService.getMovieDetails(this, movieId, getApiKey());
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        String movieImageUrl = MovieDbService.getUrlPosterFor(movie, MovieDbService.MovieSize.W342);
        Picasso.with(this).load(movieImageUrl).into(moviePoster);
        movieTitle.setText(movie.getTitle());
        movieAvgVote.setText(movie.getVoteAvarage() + "");
        movieDescription.setText(movie.getOverview());

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String releaseDatePart = String.format("%s: %s", getString(R.string.release_date), dt.format(movie.getReleaseDate()));
        movieReleaseDate.setText(releaseDatePart);
    }

    private String getApiKey() {
        return getResources().getString(R.string.api_key);
    }
}
