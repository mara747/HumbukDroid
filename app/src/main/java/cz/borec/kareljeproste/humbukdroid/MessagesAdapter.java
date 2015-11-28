package cz.borec.kareljeproste.humbukdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mara on 28.11.2015.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {
    public MessagesAdapter(Context context, List<Message> msgs) {
        super(context, 0, msgs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message msg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        // Populate the data into the template view using the data object
        tvTitle.setText(msg.getTitle());
        tvDate.setText(msg.getDate());
        tvDescription.setText(msg.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
