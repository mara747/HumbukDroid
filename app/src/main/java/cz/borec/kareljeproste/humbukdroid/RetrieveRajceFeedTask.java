package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mara on 26.11.2015.
 */
public class RetrieveRajceFeedTask extends RetrieveFeedTask {

    public RetrieveRajceFeedTask(Context aCo, ListView aLv, ProgressDialog aPd) {
        super(aCo, aLv, aPd);
    }

    protected void onPostExecute(List<Message> messages) {
        for (Message msg : messages) {
            String text = msg.getTitle().substring("humbuk | ".length());
            msg.setTitle(text);
        }

        LazyAdapter adapter=new LazyAdapter(this.mCo, messages);
        mLv.setAdapter(adapter);

        mPd.dismiss();
        mPd = null;
    }
}
