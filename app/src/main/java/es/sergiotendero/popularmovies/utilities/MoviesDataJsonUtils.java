package es.sergiotendero.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.sergiotendero.popularmovies.model.MovieData;
import es.sergiotendero.popularmovies.model.ReviewData;
import es.sergiotendero.popularmovies.model.TrailerData;

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

    // Constants for trailer data
    public static final String TRAILER_NAME = "name";
    public static final String TRAILER_SITE = "site";
    public static final String TRAILER_KEY = "key";

    // Constants for review data
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";

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

        // Convert movies data from json object to MovieData
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

    /**
     * This method parses trailers data in JSON to a List of TrailerData
     *
     * @param jsonTrailers trailers data in JSON format
     * @return List of TrailerData
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<TrailerData> getTrailersFromJson(String jsonTrailers) throws JSONException {

        List<TrailerData> parsedTrailerData = new ArrayList<TrailerData>();

        // Extract main json object
        JSONObject jsonData = new JSONObject(jsonTrailers);

        // Extract results
        JSONArray jsonList = jsonData.getJSONArray(RESULTS_LIST);

        // Convert trailers data from json object to TrailerData
        for (int i = 0; i < jsonList.length(); i++) {

            JSONObject jsonTrailer = jsonList.getJSONObject(i);

            TrailerData trailerData = new TrailerData();

            trailerData.setName(jsonTrailer.getString(MoviesDataJsonUtils.TRAILER_NAME));
            trailerData.setSite(jsonTrailer.getString(MoviesDataJsonUtils.TRAILER_SITE));
            trailerData.setKey(jsonTrailer.getString(MoviesDataJsonUtils.TRAILER_KEY));

            parsedTrailerData.add(trailerData);
        }

        return parsedTrailerData;
    }

    /**
     * This method parses reviews data in JSON to a List of ReviewData
     *
     * @param jsonReviews reviews data in JSON format
     * @return List of ReviewData
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static List<ReviewData> getReviewsFromJson(String jsonReviews) throws JSONException {

        List<ReviewData> parsedReviewData = new ArrayList<ReviewData>();

        // Extract main json object
        JSONObject jsonData = new JSONObject(jsonReviews);

        // Extract results
        JSONArray jsonList = jsonData.getJSONArray(RESULTS_LIST);

        // Convert reviews data from json object to ReviewData
        for (int i = 0; i < jsonList.length(); i++) {

            JSONObject jsonTrailer = jsonList.getJSONObject(i);

            ReviewData reviewData = new ReviewData();

            reviewData.setAuthor(jsonTrailer.getString(MoviesDataJsonUtils.REVIEW_AUTHOR));
            reviewData.setContent(jsonTrailer.getString(MoviesDataJsonUtils.REVIEW_CONTENT));

            parsedReviewData.add(reviewData);
        }

        return parsedReviewData;
    }
}
