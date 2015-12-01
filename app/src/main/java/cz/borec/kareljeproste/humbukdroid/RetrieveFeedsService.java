package cz.borec.kareljeproste.humbukdroid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Xml;

/**
 * Created by Mara on 1.12.2015.
 */
public class RetrieveFeedsService extends Service {

    protected NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        this.mBuilder = new NotificationCompat.Builder(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                while (true) {
                    try {
                        RetrieveFeedTask rftKomentare = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssKomentare), null, getApplicationContext());
                        rftKomentare.setEncoding(Xml.Encoding.ISO_8859_1);
                        rftKomentare.execute();
                        Thread.sleep(5000);
                        RetrieveFeedTask rftClanky = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssClanky), null, getApplicationContext());
                        rftClanky.setEncoding(Xml.Encoding.ISO_8859_1);
                        rftClanky.execute();
                        Thread.sleep(5000);
                        RetrieveFeedTask rftKecalroom = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssKecalroom), null, getApplicationContext());
                        rftKecalroom.setEncoding(Xml.Encoding.ISO_8859_1);
                        rftKecalroom.execute();
                        Thread.sleep(5000);
                        RetrieveFeedTask rftRajce = new RetrieveRajceFeedTask(null, null, getResources().getString(R.string.HumbukRssRajce), null, getApplicationContext());
                        rftRajce.setImgFeed(true);
                        rftRajce.execute();
                        Thread.sleep(5*60000);
                    } catch (Exception e) {
                        stopSelf();
                    }
                }
            }
        }).start();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}