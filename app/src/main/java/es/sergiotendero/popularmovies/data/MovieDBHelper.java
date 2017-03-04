package es.sergiotendero.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static es.sergiotendero.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Movie DB helper class
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MovieDBHelper";

    // The database name
    private static final String DATABASE_NAME = "movie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold movie data
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY ," +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

        Log.d(TAG, "Movie Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
