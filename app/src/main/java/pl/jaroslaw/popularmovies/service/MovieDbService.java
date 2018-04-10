package pl.jaroslaw.popularmovies.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.jaroslaw.popularmovies.MovieAdapter;
import pl.jaroslaw.popularmovies.MovieDetailsActivity;
import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.model.Results;
import pl.jaroslaw.popularmovies.utilities.MovieDbClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jokonski on 22.03.18.
 */

public class MovieDbService {
    private static final String TAG = MovieDbService.class.getSimpleName();
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/api/";

    private static MovieDbClient getMovieDbClient(final String apiKey) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        HttpUrl httpUrl = originalRequest.url();

                        HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("api_key", apiKey).build();

                        Request.Builder requestBuilder = originalRequest.newBuilder();

                        Request newRequest = requestBuilder.url(newHttpUrl).build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(MOVIE_DB_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        MovieDbClient movieDbClient = retrofit.create(MovieDbClient.class);

        return movieDbClient;
    }

    public static void listMovies(final MovieAdapter movieAdapter, int page, MoviesOrder moviesOrder, final String apiKey) {
        Log.d(TAG, "Load page " + page + " with order " + moviesOrder.name());

        Call<Results> call = getMovieDbClient(apiKey).getMoviesTopRated(moviesOrder.getDbApiName(), page +"");

        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {

                movieAdapter.appendMovies(response.body().getMovies());
                System.out.println("Success");
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    public static void appendNextPage(MovieAdapter movieAdapter, int offset, MoviesOrder moviesOrder, String apiKey) {
        listMovies(movieAdapter, offset, moviesOrder, apiKey);
    }

    public static void getMovieDetails(final MovieDetailsActivity detailsActivity, Long movieId, String apiKey) {
        Call<Movie> call = getMovieDbClient(apiKey).getMovieById(Long.toString(movieId));

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                detailsActivity.setMovie(response.body());
                System.out.println("Success");
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                System.out.println("Failure");
            }
        });
    }

    public enum MoviesOrder {
        MOST_POPULAR ("popular"),
        TOP_RATED ("top_rated");


        private String dbApiName;

        MoviesOrder(String dbApiName) {
            this.dbApiName = dbApiName;
        }

        public String getDbApiName() {
            return dbApiName;
        }
    }
}
