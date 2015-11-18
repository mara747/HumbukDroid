package cz.borec.kareljeproste.humbukdroid;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cz.borec.kareljeproste.humbukdroid.R.layout.activity_main);

        mProgressDialog = ProgressDialog.show(this, "", "Načítám poslední komentáře ...", true);
        new RetrieveFeedTask(this,(ListView) findViewById(cz.borec.kareljeproste.humbukdroid.R.id.listView),mProgressDialog).execute(getString(cz.borec.kareljeproste.humbukdroid.R.string.HumbukRssKomentare));
    }
}
