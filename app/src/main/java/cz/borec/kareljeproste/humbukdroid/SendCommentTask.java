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
import java.util.List;
import java.util.Map;


/**
 * Created by Mara on 5.12.2015.
 */
public class SendCommentTask extends AsyncTask<String, Void, Boolean> {

    public interface AsyncResponse {
        void processFinish(Boolean output);
    }

    public AsyncResponse delegate = null;

    public SendCommentTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(String... params) {
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
                    new OutputStreamWriter(os, "Windows-1250"));
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
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
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

            result.append(URLEncoder.encode(pair.getKey(), "Windows-1250"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "Windows-1250"));
        }

        return result.toString();
    }
}
