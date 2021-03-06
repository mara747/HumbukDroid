package cz.borec.kareljeproste.humbukdroid;

import android.app.Notification;
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

    public static final long REPEAT_TIME = 1000 * 60 * 5;
    public static final int MSG_ID_KOMENTARE = 0;
    public static final int MSG_ID_CLANKY = 1;
    public static final int MSG_ID_KECALROOM = 2;
    public static final int MSG_ID_FOTOGALERIE = 3;
    public static final int MSG_ID_ERR = 0;

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
                try {
                    RetrieveFeedTask rftKomentare = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssKomentareTop1), null, getApplicationContext(),MSG_ID_KOMENTARE);
                    rftKomentare.setEncoding(Xml.Encoding.ISO_8859_1);
                    rftKomentare.execute().get();
                    RetrieveFeedTask rftClanky = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssClankyTop1), null, getApplicationContext(),MSG_ID_CLANKY);
                    rftClanky.setEncoding(Xml.Encoding.ISO_8859_1);
                    rftClanky.execute().get();
                    RetrieveFeedTask rftKecalroom = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssKecalroomTop1), null, getApplicationContext(),MSG_ID_KECALROOM);
                    rftKecalroom.setEncoding(Xml.Encoding.ISO_8859_1);
                    rftKecalroom.execute().get();
                    RetrieveFeedTask rftRajce = new RetrieveFeedTask(null, null, getResources().getString(R.string.HumbukRssRajceTop1), null, getApplicationContext(),MSG_ID_FOTOGALERIE);
                    rftRajce.execute().get();
                } catch (Exception e) {
                    RetrieveFeedTask.notifyMsg(MSG_ID_ERR, "Chyba při čtení Karla - kontaktuj Máru:", e.getMessage(), mBuilder, getApplicationContext());
                }
            }
        }).start();
        stopSelf();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
