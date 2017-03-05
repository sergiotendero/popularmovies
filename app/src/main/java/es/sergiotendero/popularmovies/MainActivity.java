package es.sergiotendero.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.sergiotendero.popularmovies.data.MovieContract;
import es.sergiotendero.popularmovies.model.MovieData;
import es.sergiotendero.popularmovies.utilities.MoviesDataJsonUtils;
import es.sergiotendero.popularmovies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    public static final int SORT_BY_MOST_POPULAR = 1;
    public static final int SORT_BY_HIGHEST_RATED = 2;
    public static final int FAVORITES = 3;

    public static final String CURRENT_SCROLL_POSITION = "current_scroll_position";

    GridLayoutManager mGridLayoutManager;
    private int currentScrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Initialize recycler view component
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        // Number of columns depends on device screen resolution
        mGridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        // Restore the scroll position
        if (savedInstanceState != null) {
            currentScrollPosition = savedInstanceState.getInt(CURRENT_SCROLL_POSITION, 0);
        }

        // Recovers user preference for showing movies
        int savedPreference = readMoviesOption();
        if (savedPreference == FAVORITES) {
            showFavorites();
        } else {
            loadMoviesData(savedPreference);
        }

    }

    /**
     * Load movies data using a sort option
     *
     * @param sort indicates the sort option to be used
     */
    private void loadMoviesData(int sort) {
        showMoviesDataView();
        new FetchMoviesDataTask().execute(new Integer(sort));

    }

    /**
     * Show the movies data and hide the error message
     */
    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show the error message and hide the movies data
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieData movieData) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        // Send selected movie data to detail activity
        intent.putExtra(intent.EXTRA_TEXT, movieData);
        startActivity(intent);
    }

    public class FetchMoviesDataTask extends AsyncTask<Integer, Void, List<MovieData>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieData> doInBackground(Integer... integers) {

            // Constructs the URL using the first integer passed as parameter (which indicates sorting)
            URL moviesUrl = NetworkUtils.buildMovieDBUrl(integers[0].intValue());

            try {
                String jsonMoviesDataResponse = NetworkUtils.getResponseFromHttpUrl(moviesUrl);

                return MoviesDataJsonUtils.getMoviesDataFromJson(jsonMoviesDataResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<MovieData> moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mMovieAdapter.setMoviesData(moviesData);
                // Scroll back to previous position
                mRecyclerView.smoothScrollToPosition(currentScrollPosition);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            // Save user preference
            writeMoviesOption(SORT_BY_MOST_POPULAR);
            loadMoviesData(SORT_BY_MOST_POPULAR);
            return true;
        }

        if (id == R.id.action_rated) {
            // Save user preference
            writeMoviesOption(SORT_BY_HIGHEST_RATED);
            loadMoviesData(SORT_BY_HIGHEST_RATED);
            return true;
        }

        if (id == R.id.action_favorites) {
            // Save user preference
            writeMoviesOption(FAVORITES);
            showFavorites();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Recovers favorite movie data from a private Content provider
     * It could be better to implement as background task, but for simplicity reasons it will
     * be executed on UI thread
     */
    private void showFavorites() {

        try {

            // Queries content resolver for movie data
            Cursor cursorMovies = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

            // Creates a List of MovieData objects from cursor

            int idCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry._ID);
            int titleCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int releaseDataCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            int posterCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            int voteAverageCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            int overviewCol = cursorMovies.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);

            List<MovieData> listMovies = new ArrayList<MovieData>();

            while (cursorMovies.moveToNext()) {
                MovieData movie = new MovieData();
                movie.setId(cursorMovies.getInt(idCol));
                movie.setTitle(cursorMovies.getString(titleCol));
                movie.setReleaseDate(cursorMovies.getString(releaseDataCol));
                movie.setPosterPath(cursorMovies.getString(posterCol));
                movie.setVoteAverage(cursorMovies.getString(voteAverageCol));
                movie.setOverview(cursorMovies.getString(overviewCol));

                listMovies.add(movie);
            }
            cursorMovies.close();

            // Set movies data
            mMovieAdapter.setMoviesData(listMovies);
            showMoviesDataView();
            // Scroll back to previous position
            mRecyclerView.smoothScrollToPosition(currentScrollPosition);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage();
        }
    }

    /**
     * Reads which movies (most popular, highest rated, favorites) user selected to show last time
     *
     * @return A constant that identifies the option
     */
    private int readMoviesOption() {

        // The selected option is loaded from a private SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int defaultValue = Integer.parseInt(getResources().getString(R.string.search_option_default));
        return sharedPref.getInt(getString(R.string.search_option_key), defaultValue);
    }

    /**
     * Writes which movies (most popular, highest rated, favorites) user selected to show last time
     */
    private void writeMoviesOption(int search_option) {

        // The selected option is saved in a private SharedPreferences file
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.search_option_key), search_option);
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Saves the scroll position
        outState.putInt(CURRENT_SCROLL_POSITION, mGridLayoutManager.findFirstVisibleItemPosition());
    }

    /**
     * Calculate the number of possible columns at runtime
     *
     * @param context
     * @return number of columns to render
     */
    protected int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
