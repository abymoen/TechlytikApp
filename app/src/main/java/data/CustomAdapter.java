package data;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import techlytik.techlytik.MonitorDetailsActivity;
import techlytik.techlytik.R;

/**
 * Created by alex on 2016-06-15.
 */
public class CustomAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<HashMap<String, String>> monitors;
    private static LayoutInflater inflator = null;

    public CustomAdapter(Context context, ArrayList<HashMap<String, String>> data) {
        mContext = context;
        monitors = data;
        inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return monitors.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if(convertView == null) {
            view = inflator.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.status = (TextView)view.findViewById(R.id.statusText);
            holder.name = (TextView)view.findViewById(R.id.monitorIdText);
            holder.location = (TextView)view.findViewById(R.id.monitorLocationText);
            holder.armed = (TextView)view.findViewById(R.id.monitorArmText);
            holder.statusImage = (ImageView)view.findViewById(R.id.statusImage);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        HashMap<String, String> mMonitors = new HashMap<>();
        mMonitors = monitors.get(position);

        holder.status.setText("Status: " + mMonitors.get("status"));
        holder.name.setText(mMonitors.get("id"));
        holder.location.setText((mMonitors.get("location")));
        holder.armed.setText("Armed: " + mMonitors.get("armed"));

        String pic = holder.status.getText().toString();
        String armed = holder.armed.getText().toString();

        if(armed.equals("Armed: no")) {
            holder.statusImage.setImageResource(R.drawable.black);
        } else if(pic.equals("Status: optimal") && armed.equals("Armed: yes")){
            holder.statusImage.setImageResource(R.drawable.green);
        } else {
            holder.statusImage.setImageResource(R.drawable.red);
        }

        return view;
    }

    public class ViewHolder {
        private TextView status, armed, name, location;
        private ImageView statusImage;
    }
}
