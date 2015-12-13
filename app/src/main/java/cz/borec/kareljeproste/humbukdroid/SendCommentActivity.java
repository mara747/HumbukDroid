package cz.borec.kareljeproste.humbukdroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SendCommentActivity extends AppCompatActivity {

    private final long MINUTE_IN_MILLISECONDS = 60 * 1000;

    private Spinner mSpinnerArticle;
    private SpinnerArticleAdapter mAdapter;
    private List<Message> mMsgList;
    private Context mCtx;

    public SendCommentActivity() {
        mMsgList = new ArrayList<Message>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);

        final int secNum;
        Bundle extras = getIntent().getExtras();
        secNum = extras.getInt(MainActivity.PlaceholderFragment.ARG_SECTION_NUMBER,MainActivity.PlaceholderFragment.ARG_SECTION_KOMENTARE);
        setTitle(MainActivity.getTitleHumbukString());

        mCtx = this;
        SharedPreferences settings = mCtx.getSharedPreferences("HumbukDroidPrefsName", 0);
        ((EditText)findViewById(R.id.editTextName)).setText(settings.getString("name",""));
        ((EditText)findViewById(R.id.editTextEmail)).setText(settings.getString("email",""));
        ((EditText)findViewById(R.id.editTextText)).setText(settings.getString("text",""));
        mAdapter = new SpinnerArticleAdapter(this,android.R.layout.simple_spinner_item,mMsgList);

        Message msg = new Message();
        if (secNum==MainActivity.PlaceholderFragment.ARG_SECTION_KOMENTARE)
        {
            msg.setTitle(getResources().getString(R.string.SendCommentDefaultTitle));
            mMsgList.add(msg);
            ProgressDialog pd = ProgressDialog.show(mCtx, "", getResources().getString(R.string.Loading), true);
            new RetrieveFeedTask(mMsgList, mAdapter,getResources().getString(R.string.HumbukRssClanky),pd,this).execute();
        } else if (secNum==MainActivity.PlaceholderFragment.ARG_SECTION_KECALROOM)
        {
            msg = new Message();
            msg.setTitle( getResources().getString(R.string.SendCommentKecalroomTitle));
            msg.setLink("http://www.kareljeproste.borec.cz/index1.php?clanek=kecalroom&p=0");
            mMsgList.add(msg);
        }

        mSpinnerArticle = ((Spinner)findViewById(R.id.spinnerArticle));
        mSpinnerArticle.setAdapter(mAdapter);

        mSpinnerArticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                mAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMsgList.get(0).getLink();
                final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
                final String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
                final String text = ((EditText) findViewById(R.id.editTextText)).getText().toString();
                final URL url = mMsgList.get(mSpinnerArticle.getSelectedItemPosition()).getLink();
                final SharedPreferences settings = mCtx.getSharedPreferences("HumbukDroidPrefsName", 0);
                final SharedPreferences.Editor editor = settings.edit();
                if (url!=null)
                {
                    if(name.isEmpty()) {
                        Toast.makeText(mCtx,getResources().getString(R.string.SendCommentNameErr),Toast.LENGTH_SHORT).show();
                    } else if (text.isEmpty()) {
                        Toast.makeText(mCtx,getResources().getString(R.string.SendCommentTextErr),Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String lnk = url.toExternalForm();
                                    new SendCommentTask(
                                            new SendCommentTask.AsyncResponse(){
                                                @Override
                                                public void processFinish(Boolean result){
                                                    if (result) {
                                                        editor.putString("text", "");
                                                        editor.putBoolean("myMsgSent", true);
                                                        editor.commit();
                                                        Toast.makeText(mCtx,getResources().getString(R.string.SendCommentMsgOK),Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        editor.putString("text", text);
                                                        editor.commit();
                                                        Toast.makeText(mCtx,getResources().getString(R.string.SendCommentMsgErr),Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                    ).execute(lnk, "4", name, email, text).get();

                                    Calendar c = Calendar.getInstance();
                                    editor.putLong("pubDate", c.getTimeInMillis());
                                    editor.commit();
                                } catch (Exception e) {
                                    editor.putString("text", text);
                                    editor.commit();
                                    Toast.makeText(mCtx,getResources().getString(R.string.SendCommentMsgErr),Toast.LENGTH_LONG).show();
                                }
                            }
                        }).start();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.commit();
                        finish();
                    }
                } else {
                    editor.putString("text", text);
                    editor.commit();
                    Toast.makeText(mCtx,getResources().getString(R.string.SendCommentMsgNoLink),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}