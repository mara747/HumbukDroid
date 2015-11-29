package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mara on 17.11.2015.
 */
public class RetrieveFeedTask extends AsyncTask<String, Void, List<Message>> {

    private Exception mException;
    protected ProgressDialog mPd;
    protected List<Message> mMesssages;
    private String mRssUrl;
    private boolean mImgFeed = false;
    protected BaseAdapter mAdp;
    private Xml.Encoding mEncoding = Xml.Encoding.UTF_8;

    public RetrieveFeedTask(List<Message> aMsg, BaseAdapter aAdp, String aRssUrl, ProgressDialog aPd) {
        this.mPd = aPd;
        this.mRssUrl = aRssUrl;
        this.mMesssages = aMsg;
        this.mAdp=aAdp;
    }

    public void setImgFeed(boolean aImgFeed){
        mImgFeed = aImgFeed;
    }
    public void setEncoding(Xml.Encoding aEncoding) { mEncoding = aEncoding; }

    protected List<Message>  doInBackground(String... urls) {
        try {
            AndroidSaxFeedParser asfd = new AndroidSaxFeedParser(mRssUrl, mEncoding);
            asfd.setContainImg(mImgFeed);
            return asfd.parse();
        } catch (Exception e) {
            this.mException = e;
        }
        return null;
    }

    protected void onPostExecute(List<Message> messages) {
        if (messages!=null)
            {
            mMesssages.clear();
            mMesssages.addAll(messages);
            mAdp.notifyDataSetChanged();
            }

        if (mPd != null) {
            mPd.dismiss();
        }
    }
}
