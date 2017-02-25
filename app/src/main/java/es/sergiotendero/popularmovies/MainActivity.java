package es.sergiotendero.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        // Initialize recycler view component
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        // It will show 4 movies by row
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);


        // By default load most popular movies
        loadMoviesData(SORT_BY_MOST_POPULAR);

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
            loadMoviesData(SORT_BY_MOST_POPULAR);
            return true;
        }

        if (id == R.id.action_rated) {
            loadMoviesData(SORT_BY_HIGHEST_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
