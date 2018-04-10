package pl.jaroslaw.popularmovies.utilities;

import java.util.List;

import pl.jaroslaw.popularmovies.model.Movie;
import pl.jaroslaw.popularmovies.model.Results;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jokonski on 21.03.18.
 */

public interface MovieDbClient {

    //@GET("/3/movie/popular")
    //Call<Results> getMoviesPopular();

    @GET("/3/movie/{order_type}")//top_rated")
    Call<Results> getMoviesTopRated(@Path("order_type") String orderType, @Query("page") String page);


    @GET("/3/movie/{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") String movie_id);
}
