package cz.borec.kareljeproste.humbukdroid;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(MainActivity.getTitleHumbukString());

        String version = "HumbukDroid";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version += " " + pInfo.versionName;
        } catch (Exception e)
        {
        }
        ((TextView)findViewById(R.id.textViewVersion)).setText(version);
    }
}
