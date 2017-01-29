package es.sergiotendero.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.sergiotendero.popularmovies.utilities.MoviesDataJsonUtils;

/**
 * Activity for showing a movie data in detail
 */
public class MovieDetailActivity extends AppCompatActivity {

    // In detailed movie activity images will be 342 sized
    private final String picassoBaseURLw342 = "http://image.tmdb.org/t/p/w342/";

    private TextView mMovieDetail;
    private ImageView mMoviePoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        mMovieDetail = (TextView) findViewById(R.id.tv_movie_detail);
        mMoviePoster = (ImageView) findViewById(R.id.i_movie_poster);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String[] movieData = intent.getStringArrayExtra(Intent.EXTRA_TEXT);

            // Format movie data in a String
            StringBuffer movieDataFormatted = new StringBuffer();
            movieDataFormatted.append("Title: " + movieData[MoviesDataJsonUtils.POSITION_TITLE] + "\n\n");
            movieDataFormatted.append("Release date: " + movieData[MoviesDataJsonUtils.POSITION_RELEASE_DATE] + "\n\n");
            movieDataFormatted.append("Vote average: " + movieData[MoviesDataJsonUtils.POSITION_VOTE_AVERAGE] + "\n\n");
            movieDataFormatted.append("Plot synopsis: " + movieData[MoviesDataJsonUtils.POSITION_OVERVIEW]);

            mMovieDetail.setText(movieDataFormatted.toString());

            // Show movie poster
            Picasso.with(this.getApplicationContext()).load(picassoBaseURLw342 + movieData[MoviesDataJsonUtils.POSITION_POSTER_PATH]).into(mMoviePoster);
        }
    }
}
