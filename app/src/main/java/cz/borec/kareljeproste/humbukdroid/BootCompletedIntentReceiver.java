package cz.borec.kareljeproste.humbukdroid;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Mara on 2.12.2015.
 */
public class BootCompletedIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar cal = Calendar.getInstance();
        Intent intent2 = new Intent(context, RetrieveFeedsService.class);
        PendingIntent pintent = PendingIntent.getService(context, 0, intent2, 0);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // schedule for every 30 seconds
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), RetrieveFeedsService.REPEAT_TIME, pintent);
    }
}
