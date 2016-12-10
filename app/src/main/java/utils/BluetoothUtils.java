package utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import techlytik.techlytik.ConnectMonitorActivity;

/**
 * Created by alex on 2016-08-25.
 */
public class BluetoothUtils {

    public static boolean checkBluetooth(BluetoothAdapter bluetoothAdapter) {
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestUserBluetooth(Activity act) {
        Intent enableBLEIntet = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        act.startActivityForResult(enableBLEIntet, ConnectMonitorActivity.REQUEST_ENABLE_BT);
    }

    public static void toast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_LONG);
    }
}
