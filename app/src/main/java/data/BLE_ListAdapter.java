package data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import techlytik.techlytik.BTLE_Device;
import techlytik.techlytik.R;

/**
 * Created by alex on 2016-08-25.
 */
public class BLE_ListAdapter extends BaseAdapter {

    private Context mContext;
    private HashMap<String, BTLE_Device> data;
    private static LayoutInflater inflator = null;

    public BLE_ListAdapter(Context context, HashMap<String,BTLE_Device> data) {
        mContext = context;
        this.data = data;
        inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
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
        ViewHolderBLE holder;

        if(convertView == null) {
            view = inflator.inflate(R.layout.ble_row, null);
            holder = new ViewHolderBLE();
            holder.name = (TextView)view.findViewById(R.id.deviceNameBLE);
            view.setTag(holder);
        } else {
            holder = (ViewHolderBLE) view.getTag();
        }

        holder.name.setText("Omicron Central");
        return view;
    }

    public class ViewHolderBLE {
        private TextView name;
    }
}
