package techlytik.techlytik;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import utils.BluetoothUtils;

/**
 * Created by alex on 2016-08-25.
 */
public class BroadcastReciever_BTState extends BroadcastReceiver {
    Context activityContext;

    public BroadcastReciever_BTState(Context ctx) {
        this.activityContext = ctx;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

        switch(state) {
            case BluetoothAdapter.STATE_OFF:
                BluetoothUtils.toast(activityContext, "Bluetooth is OFF");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                BluetoothUtils.toast(activityContext, "Bluetooth is truning OFF");
                break;
            case BluetoothAdapter.STATE_ON:
                BluetoothUtils.toast(activityContext, "Bluetooth is ON");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                BluetoothUtils.toast(activityContext, "Bluetooth is turning ON");
                break;
        }
    }
}
