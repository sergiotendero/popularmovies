package es.sergiotendero.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.sergiotendero.popularmovies.model.MovieData;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    // In main activity images will be 185 sized
    private final String picassoBaseURLw185 = "http://image.tmdb.org/t/p/w185/";

    // Store movies data recovered form internet
    private List<MovieData> moviesData;

    private MovieAdapterOnClickHandler mMovieAdapterOnClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        mMovieAdapterOnClickHandler = movieAdapterOnClickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieAdapterViewHolder(inflater.inflate(R.layout.movie_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        // I need a context for using Picasso
        // I obtain form OnClinkHandler because in this app I know is an activity class
        Context context = ((Activity) mMovieAdapterOnClickHandler).getApplicationContext();

        // Load movie poster
        Picasso.with(context).load(picassoBaseURLw185 + moviesData.get(position).getPosterPath()).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (null == moviesData) return 0;
        return moviesData.size();
    }

    public void setMoviesData(List<MovieData> moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Receives the event and invokes the handler
            mMovieAdapterOnClickHandler.onClick(moviesData.get(getAdapterPosition()));
        }
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieData movieData);
    }
}
