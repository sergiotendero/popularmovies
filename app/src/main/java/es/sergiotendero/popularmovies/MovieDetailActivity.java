package es.sergiotendero.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import es.sergiotendero.popularmovies.data.MovieContract;
import es.sergiotendero.popularmovies.databinding.ActivityMovieDetailBinding;
import es.sergiotendero.popularmovies.model.MovieData;
import es.sergiotendero.popularmovies.model.ReviewData;
import es.sergiotendero.popularmovies.model.TrailerData;
import es.sergiotendero.popularmovies.utilities.MoviesDataJsonUtils;
import es.sergiotendero.popularmovies.utilities.NetworkUtils;

/**
 * Activity for showing a movie data in detail
 */
public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";

    // In detailed movie activity images will be 342 sized
    private final String picassoBaseURLw342 = "http://image.tmdb.org/t/p/w342/";

    // Base youtube url for watching videos
    private final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    // YouTube site name
    private final String YOUTUBE_SITE = "YouTube";

    // Object for accessing views in layout
    private ActivityMovieDetailBinding mBinding;

    private MovieData mMovieData;

    // List of TrailerData objects recovered
    private List<TrailerData> mTrailerData;

    // List of ReviewData objects recovered
    private List<ReviewData> mReviewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Associate object to layout for binding data
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieData = (MovieData) intent.getParcelableExtra(Intent.EXTRA_TEXT);

            mBinding.tvMovieTitle.setText(mMovieData.getTitle());
            mBinding.tvMovieReleaseDate.setText(getString(R.string.movie_detail_release_date_label) + ": " + mMovieData.getReleaseDate());
            mBinding.tvMovieVoteAverage.setText(getString(R.string.movie_detail_vote_average_label) + ": " + mMovieData.getVoteAverage());
            mBinding.tvMovieOverview.setText(mMovieData.getOverview());

            // Show movie poster
            Picasso.with(this.getApplicationContext()).load(picassoBaseURLw342 + mMovieData.getPosterPath()).into(mBinding.iMoviePoster);

            // Load trailers & reviews of the selected movie
            new FetchTrailersAndReviewsDataTask().execute();
        }

        ToggleButton toggleButton = mBinding.tbFavorite;

        // Check if its favorite
        toggleButton.setChecked(isFavorite());

        // Defines favorite switch behaviour
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Save movie as favorite
                    saveAsFavorite();
                } else {
                    // Delete movie as favorite
                    deleteAsFavorite();
                }
            }
        });
    }

    /**
     * Show trailers data in a ListView
     */
    private void showTrailers() {

        // Recover the ListView object
        ListView trailersListView = mBinding.lvTrailers;

        // Creates an adapter of trailer data
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, mTrailerData);

        // Associate the adapter to the ListView which render the data
        trailersListView.setAdapter(trailerAdapter);

        // Define a handler on item clicked to show the selected trailer
        trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // Get trailer data
                TrailerData trailer = (TrailerData) adapterView.getItemAtPosition(pos);

                // If trailer site is youtube an intent for watching it will launch
                if (YOUTUBE_SITE.equals(trailer.getSite())) {
                    // Show selected trailer
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + trailer.getKey())));
                } else {
                    // Display a Toast message indicating how to watch the trailer
                    Toast.makeText(
                            adapterView.getContext(),
                            "Visit " + trailer.getSite() + " to watch this trailer",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Show reviews data in a ListView
     */
    private void showReviews() {

        // Recover the ListView object
        ListView reviewsListView = mBinding.lvReviews;

        // Creates a generic adapter of reviews data
        ArrayAdapter<ReviewData> reviewsAdapter = new ArrayAdapter<ReviewData>(this, R.layout.review_list_item, mReviewData);

        // Associate the adapter to the ListView which render the data
        reviewsListView.setAdapter(reviewsAdapter);
    }

    /**
     * Save movie as favorite in content provider
     */
    private void saveAsFavorite() {
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the movie data into the ContentValues
        contentValues.put(MovieContract.MovieEntry._ID, mMovieData.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovieData.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovieData.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovieData.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovieData.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovieData.getOverview());
        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        Log.d(TAG, "Favorite inserted: " + uri);
    }

    /**
     * Delete movie as favorite in content provider
     */
    private void deleteAsFavorite() {

        // Get id to construct URL
        String stringId = Integer.toString(mMovieData.getId());
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // Remove movie form favorites
        int deleted = getContentResolver().delete(uri, null, null);

        if (deleted > 0) {
            Log.d(TAG, "Favorite deleted");
        }
    }

    /**
     * Query if the movie is saved as favorite
     *
     * @return true if its favorite false otherwise
     */
    private boolean isFavorite() {

        boolean isFavorite = false;
        Cursor cursor = null;

        try {
            // Get id to construct URL
            String stringId = Integer.toString(mMovieData.getId());
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            // Query if movie is favorite
            cursor = getContentResolver().query(uri, null, null, null, null);
            // If returns a row is supposed to be favorite
            isFavorite = cursor.moveToNext();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isFavorite;
    }


    public class FetchTrailersAndReviewsDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Constructs the URLs for querying trailers & reviews
            URL trailersUrl = NetworkUtils.buildMovieTrailersDBUrl(mMovieData.getId());
            URL reviewsUrl = NetworkUtils.buildMovieReviewsDBUrl(mMovieData.getId());

            try {
                // Load trailers & reviews
                String jsonTrailersDataResponse = NetworkUtils.getResponseFromHttpUrl(trailersUrl);
                String jsonReviewsDataResponse = NetworkUtils.getResponseFromHttpUrl(reviewsUrl);

                mTrailerData = MoviesDataJsonUtils.getTrailersFromJson(jsonTrailersDataResponse);
                mReviewData = MoviesDataJsonUtils.getReviewsFromJson(jsonReviewsDataResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Once loaded show trailers & reviews data
            showTrailers();
            showReviews();
        }
    }
}
