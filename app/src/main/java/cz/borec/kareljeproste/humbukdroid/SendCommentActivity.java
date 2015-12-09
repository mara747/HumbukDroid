package cz.borec.kareljeproste.humbukdroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.List;

public class SendCommentActivity extends AppCompatActivity {

    private Spinner mSpinnerArticle;
    private SpinnerArticleAdapter mAdapter;
    private List<Message> mMsgList;
    private Context mCtx;

    public SendCommentActivity() {
        mMsgList = new ArrayList<Message>();
        Message msg = new Message();
        msg.setTitle("Seznam článků se zatím nenačetl ...");
        mMsgList.add(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        setTitle(MainActivity.getTitleHumbukString());
        mCtx = this;
        mAdapter = new SpinnerArticleAdapter(this,android.R.layout.simple_spinner_item,mMsgList);
        mSpinnerArticle = ((Spinner)findViewById(R.id.spinnerArticle));
        mSpinnerArticle.setAdapter(mAdapter);
        ProgressDialog pd = ProgressDialog.show(mCtx, "", getResources().getString(R.string.Loading), true);
        new RetrieveFeedTask(mMsgList, mAdapter,getResources().getString(R.string.HumbukRssClanky),pd,this).execute();

        mSpinnerArticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                mAdapter.getItem(position);
                //Toast.makeText(mCtx,msg.getTitle(),Toast.LENGTH_SHORT).show();
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
                String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
                String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
                String text = ((EditText) findViewById(R.id.editTextText)).getText().toString();

                URL url = mMsgList.get(mSpinnerArticle.getSelectedItemPosition()).getLink();
                if (url!=null)
                {
                    String lnk = url.toExternalForm();
                    new SendCommentTask().execute(lnk, "4", name, email, text);
                }
                finish();
            }
        });

    }

}