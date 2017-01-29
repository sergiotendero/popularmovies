package es.sergiotendero.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import es.sergiotendero.popularmovies.BuildConfig;
import es.sergiotendero.popularmovies.MainActivity;

/**
 * These utilities will be used to communicate with the The Movie Database API.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIEDB_URL_POPULAR =
            "http://api.themoviedb.org/3/movie/popular";

    private static final String MOVIEDB_URL_RATED =
            "http://api.themoviedb.org/3/movie/top_rated";


    private final static String API_PARAM = "api_key";

    // Api key value from configuration
    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;


    /**
     * Builds the URL used to talk to the movie server using a sort value.
     *
     * @param sort The sort option to query movies data
     * @return The URL to use to query the server
     */
    public static URL buildMovieDBUrl(int sort) {

        // sort value indicates which base url has to be used
        String baseURL = sort == MainActivity.SORT_BY_MOST_POPULAR ? MOVIEDB_URL_POPULAR : MOVIEDB_URL_RATED;

        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
