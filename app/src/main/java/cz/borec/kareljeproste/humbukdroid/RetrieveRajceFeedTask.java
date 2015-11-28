package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mara on 26.11.2015.
 */
public class RetrieveRajceFeedTask extends RetrieveFeedTask {

    //public RetrieveRajceFeedTask(List<Message> aMessages, ProgressDialog aPd) {
    public RetrieveRajceFeedTask(List<Message> aMsg, BaseAdapter aAdp, String aRssUrl, ProgressDialog aPd) {
        super(aMsg, aAdp, aRssUrl, aPd);
    }

    protected void onPostExecute(List<Message> messages) {
        for (Message msg : messages) {
            String text = msg.getTitle().substring("humbuk | ".length());
            msg.setTitle(text);
        }

        mMesssages.clear();
        mMesssages.addAll(messages);
        mAdp.notifyDataSetChanged();
        if (mPd != null) {
            mPd.dismiss();
        }
    }
}
