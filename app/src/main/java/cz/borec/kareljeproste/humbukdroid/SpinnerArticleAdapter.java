package cz.borec.kareljeproste.humbukdroid;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mara on 7.12.2015.
 */
public class SpinnerArticleAdapter  extends ArrayAdapter<Message> {

    private Context mContext;
    private List<Message> mMessages;

    public SpinnerArticleAdapter(Context context, int textViewResourceId,
                                 List<Message> values) {
        super(context, textViewResourceId, values);
        this.mContext = context;
        this.mMessages = values;
    }

    public int getCount(){
        return mMessages.size();
    }

    public Message getItem(int position){
        return mMessages.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        label.setTextColor(0xFF4F4F4F);
        label.setMinLines(2);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setText(mMessages.get(position).getTitle());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        label.setTextColor(0xFF4F4F4F);
        label.setMinLines(2);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setText(mMessages.get(position).getTitle());
        return label;
    }
}
