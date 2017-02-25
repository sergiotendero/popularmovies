package es.sergiotendero.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.sergiotendero.popularmovies.model.MovieData;

/**
 * Utility functions to handle The Movie Database API JSON data
 */
public final class MoviesDataJsonUtils {

    public static final String RESULTS_LIST = "results";
    public static final String ATT_ID = "id";
    public static final String ATT_TITLE = "title";
    public static final String ATT_RELEASE_DATE = "release_date";
    public static final String ATT_POSTER_PATH = "poster_path";
    public static final String ATT_VOTE_AVERAGE = "vote_average";
    public static final String ATT_OVERVIEW = "overview";

    /**
     * This method parses movie data in JSON to a List of MovieData
     *
     * @param jsonDataStr movies data in JSON format
     * @return List of MovieData
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<MovieData> getMoviesDataFromJson(String jsonDataStr) throws JSONException {

        List<MovieData> parsedMoviesData = new ArrayList<MovieData>();

        // Extract main json object
        JSONObject jsonData = new JSONObject(jsonDataStr);

        // Extract results
        JSONArray jsonMovieList = jsonData.getJSONArray(RESULTS_LIST);

        // Convert movies data from json object to String[6]
        for (int i = 0; i < jsonMovieList.length(); i++) {

            JSONObject jsonMovie = jsonMovieList.getJSONObject(i);

            MovieData movieData = new MovieData();

            movieData.setId(Integer.parseInt(jsonMovie.getString(MoviesDataJsonUtils.ATT_ID)));
            movieData.setTitle(jsonMovie.getString(MoviesDataJsonUtils.ATT_TITLE));
            movieData.setReleaseDate(jsonMovie.getString(MoviesDataJsonUtils.ATT_RELEASE_DATE));
            movieData.setPosterPath(jsonMovie.getString(MoviesDataJsonUtils.ATT_POSTER_PATH));
            movieData.setVoteAverage(jsonMovie.getString(MoviesDataJsonUtils.ATT_VOTE_AVERAGE));
            movieData.setOverview(jsonMovie.getString(MoviesDataJsonUtils.ATT_OVERVIEW));

            parsedMoviesData.add(movieData);
        }

        return parsedMoviesData;
    }
}
