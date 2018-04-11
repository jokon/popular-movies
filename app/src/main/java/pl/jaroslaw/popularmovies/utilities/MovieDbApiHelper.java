package pl.jaroslaw.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import pl.jaroslaw.popularmovies.R;
import pl.jaroslaw.popularmovies.model.Movie;

/**
 * Created by jokonski on 18.03.18.
 */

public class MovieDbApiHelper {
    private static int api_key_resource_id = R.string.api_key;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p"; // /w500/mMZRKb3NVo5ZeSPEIaNW9buLWQ0.jpg";

    //"w92", "w154", "w185", "w342", "w500", "w780" -- default is "w185"
    public static final String IMAGE_SIZE_DEFAULT = "w185";
    public static final String IMAGE_SIZE_DETAILED = "w342";


    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";

    private static final String MOVIE_DETAILS_ENDPOINT = "movie/?";
    private static final String MOVIES_POPULAR_ENDPOINT = "movie/popular";
    private static final String MOVIES_TOPRATED_ENDPOINT = "movie/top_rated";

    private static String getApiKey(Context context) {
        return context.getResources().getString(R.string.api_key);
    }

    public static String getUrlPosterFor(Movie movie, String size) {
        return IMAGE_BASE_URL + "/" + size + "/" + movie.getPosterPath();
    }

    public static String getUrlPosterFor(Movie movie) {
        return getUrlPosterFor(movie, IMAGE_SIZE_DEFAULT);
    }

    public static URL getPopularMovies(Context context) throws MalformedURLException {
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon().appendEncodedPath(MOVIES_POPULAR_ENDPOINT).appendQueryParameter("api_key", getApiKey(context)).build();

        return new URL(uri.toString());
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }


        } finally {
            connection.disconnect();
        }

    }


}
