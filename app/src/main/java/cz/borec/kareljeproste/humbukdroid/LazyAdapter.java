package cz.borec.kareljeproste.humbukdroid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LazyAdapter extends BaseAdapter {
    
    private Context context;
    private  List<Message> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Context a,  List<Message> d) {
        context = a;
        data=d;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(context.getApplicationContext());
        imageLoader=new ImageLoader(context);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = LayoutInflater.from(context).inflate(R.layout.item_rajce_message, parent, false);

        TextView text=(TextView)vi.findViewById(R.id.rajceTextView);;
        ImageView image=(ImageView)vi.findViewById(R.id.rajceImageView);
        text.setText(data.get(position).getTitle());
        imageLoader.DisplayImage(data.get(position).getImgLink().toString(), image);
        return vi;
    }
}