package es.sergiotendero.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.sergiotendero.popularmovies.model.TrailerData;

/**
 * Adapter class for showing trailer data in a ListView
 */
public class TrailerAdapter extends ArrayAdapter<TrailerData> {

    public TrailerAdapter(Context context, List<TrailerData> data) {
        // invokes parent with context and data
        super(context, 0, data);
    }

    @NonNull
    @Override
    /**
     * Creates & populates a view item with trailer data
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recovers the trailer item from this position of the datasource
        TrailerData trailerData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvTrailerName = (TextView) convertView.findViewById(R.id.tv_trailer_name);
        // Populate the data into the template view using the data object
        tvTrailerName.setText(trailerData.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
