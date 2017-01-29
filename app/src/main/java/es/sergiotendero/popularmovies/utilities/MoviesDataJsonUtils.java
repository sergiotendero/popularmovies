package es.sergiotendero.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static final int POSITION_ID = 0;
    public static final int POSITION_TITLE = 1;
    public static final int POSITION_RELEASE_DATE = 2;
    public static final int POSITION_POSTER_PATH = 3;
    public static final int POSITION_VOTE_AVERAGE = 4;
    public static final int POSITION_OVERVIEW = 5;


    /**
     * This method parses movie data in JSON to a bi-dimensional String array
     * For each movie it will be a String array with its data.
     * Movie Data structure:
     * parsedMoviesData [i][0] -> id
     * parsedMoviesData [i][1] -> title
     * parsedMoviesData [i][2] -> release_date
     * parsedMoviesData [i][3] -> poster_path
     * parsedMoviesData [i][4] -> vote_average
     * parsedMoviesData [i][5] -> overview
     *
     * @param jsonDataStr movies data in JSON format
     * @return bi-dimensional String array with movies data
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static String[][] getMoviesDataFromJson(String jsonDataStr) throws JSONException {

        String[][] parsedMoviesData = null;

        // Extract main json object
        JSONObject jsonData = new JSONObject(jsonDataStr);

        // Extract results
        JSONArray jsonMovieList = jsonData.getJSONArray(RESULTS_LIST);

        // Initialize movies data structure (each movie will have six positions of data)
        parsedMoviesData = new String[jsonMovieList.length()][6];


        // Convert movies data from json object to String[6]
        for (int i = 0; i < jsonMovieList.length(); i++) {

            JSONObject jsonMovie = jsonMovieList.getJSONObject(i);

            parsedMoviesData[i][POSITION_ID] = jsonMovie.getString(MoviesDataJsonUtils.ATT_ID);
            parsedMoviesData[i][POSITION_TITLE] = jsonMovie.getString(MoviesDataJsonUtils.ATT_TITLE);
            parsedMoviesData[i][POSITION_RELEASE_DATE] = jsonMovie.getString(MoviesDataJsonUtils.ATT_RELEASE_DATE);
            parsedMoviesData[i][POSITION_POSTER_PATH] = jsonMovie.getString(MoviesDataJsonUtils.ATT_POSTER_PATH);
            parsedMoviesData[i][POSITION_VOTE_AVERAGE] = jsonMovie.getString(MoviesDataJsonUtils.ATT_VOTE_AVERAGE);
            parsedMoviesData[i][POSITION_OVERVIEW] = jsonMovie.getString(MoviesDataJsonUtils.ATT_OVERVIEW);

        }

        return parsedMoviesData;
    }
}
