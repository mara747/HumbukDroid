package cz.borec.kareljeproste.humbukdroid;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SendCommentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_comment);
        setTitle(MainActivity.getTitleHumbukString());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
                String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
                String text = ((EditText)findViewById(R.id.editTextText)).getText().toString();
                new SendCommentTask().execute("http://www.kareljeproste.borec.cz/index1.php?clanek=kecalroom&p=0", "4", name, email, text);
                Snackbar.make(view, "Odeslano", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });

    }
}
