package cz.borec.kareljeproste.humbukdroid;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


/**
 * Created by Mara on 5.12.2015.
 */
public class SendCommentTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            ContentValues paramsPost = new ContentValues();
            paramsPost.put("spam", params[1]);
            paramsPost.put("user", params[2]);
            paramsPost.put("email", params[3]);
            paramsPost.put("text", params[4]);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "ISO_8859_1"));
            writer.write(getQuery(paramsPost));
            writer.flush();
            writer.close();

            InputStream is = conn.getInputStream();
            is.read();
            is.close();

            os.close();

            conn.connect();
        } catch (Exception e)
        {
            return null;
        }
        return null;
    }

    private String getQuery(ContentValues params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> pair : params.valueSet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "ISO_8859_1"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "ISO_8859_1"));
        }

        return result.toString();
    }
}
