package cz.borec.kareljeproste.humbukdroid;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    protected NotificationCompat.Builder mBuilder;
    protected Context mCo;

    public RetrieveFeedTask(List<Message> aMsg, BaseAdapter aAdp, String aRssUrl, ProgressDialog aPd, Context aCo) {
        this.mPd = aPd;
        this.mRssUrl = aRssUrl;
        this.mMesssages = aMsg;
        this.mAdp=aAdp;
        this.mCo=aCo;
        mBuilder = new NotificationCompat.Builder(aCo);
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
        if (messages!=null && mMesssages!=null)
            {
            mMesssages.clear();
            mMesssages.addAll(messages);
            if (mAdp!=null)
                mAdp.notifyDataSetChanged();
            }

        if (mPd != null) {
            mPd.dismiss();
        }
        else if(messages!=null) {
            Message lastMsg =  messages.get(0);
            newMsgNotify(0, lastMsg.getTitle(),lastMsg.getDescription(),lastMsg.getRawDate());
        }
    }

    protected void newMsgNotify(int aId, String aTitle, String aText, Date aPubDate) {
        SharedPreferences settings = mCo.getSharedPreferences("HumbukDroidPrefsName", 0);
        Long prefPubDate = settings.getLong("pubDate", 0);

        SharedPreferences.Editor editor = settings.edit();
        if (prefPubDate!=0)
        {
            if (aPubDate.getTime()>prefPubDate)
            {
                editor.putLong("pubDate", aPubDate.getTime());
                mBuilder.setSmallIcon(R.drawable.slunce);
                mBuilder.setContentTitle(aTitle);
                mBuilder.setContentText(aText);
                NotificationManager mNotifyMgr =
                        (NotificationManager) mCo.getSystemService(mCo.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(aId, mBuilder.build());
            }

        }
        else
        {
            Calendar c = Calendar.getInstance();
            editor.putLong("pubDate", c.getTimeInMillis());
        }
        editor.commit();
    }
}
