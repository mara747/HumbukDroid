package cz.borec.kareljeproste.humbukdroid;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SendCommentActivity extends AppCompatActivity {

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
        setTitle(MainActivity.getTitleHumbukString());
        mCtx = this;
        mAdapter = new SpinnerArticleAdapter(this,android.R.layout.simple_spinner_item,mMsgList);
        mSpinnerArticle = ((Spinner)findViewById(R.id.spinnerArticle));
        mSpinnerArticle.setAdapter(mAdapter);
        new RetrieveFeedTask(mMsgList, mAdapter,getResources().getString(R.string.HumbukRssClanky),null,this).execute();

        mSpinnerArticle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Message msg = mAdapter.getItem(position);
                Toast.makeText(mCtx,msg.getTitle(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
                String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
                String text = ((EditText)findViewById(R.id.editTextText)).getText().toString();
                new SendCommentTask().execute("http://www.kareljeproste.borec.cz/index1.php?clanek=kecalroom&p=0", "4", name, email, text);
                finish();
            }
        });

    }

}