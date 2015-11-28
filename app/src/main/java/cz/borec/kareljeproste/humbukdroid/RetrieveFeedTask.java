package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mara on 17.11.2015.
 */
public class RetrieveFeedTask extends AsyncTask<String, Void, List<Message>> {

    private Exception mException;
    protected ListView mLv;
    protected Context mCo;
    protected ProgressDialog mPd;
    private boolean mImgFeed = false;
    private Xml.Encoding mEncoding = Xml.Encoding.UTF_8;

    public RetrieveFeedTask(Context aCo, ListView aLv, ProgressDialog aPd) {
        this.mCo = aCo;
        this.mLv = aLv;
        this.mPd = aPd;
    }

    public void setImgFeed(boolean aImgFeed){
        mImgFeed = aImgFeed;
    }
    public void setEncoding(Xml.Encoding aEncoding) { mEncoding = aEncoding; }

    protected List<Message>  doInBackground(String... urls) {
        try {
            AndroidSaxFeedParser asfd = new AndroidSaxFeedParser(urls[0], mEncoding);
            asfd.setContainImg(mImgFeed);
            return asfd.parse();
        } catch (Exception e) {
            this.mException = e;
        }
        return null;
    }

    protected void onPostExecute(List<Message> messages) {
        ArrayList<Message> arrayOfUsers = new ArrayList<>();
        MessagesAdapter adapter = new MessagesAdapter(this.mCo, messages);
        mLv.setAdapter(adapter);

        mPd.dismiss();
        mPd = null;
    }

    /* ADAPTER */
    private class MessagesAdapter extends ArrayAdapter<Message> {
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
}
