package es.sergiotendero.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Movie Contract class for SQLite scheme & Content Provider functionality
 */
public class MovieContract {

    // Constants for accessing through content provider
    public static final String AUTHORITY = "es.sergiotendero.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Path for movies directory
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Movie table name
        public static final String TABLE_NAME = "movies";

        // Movie column names
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
