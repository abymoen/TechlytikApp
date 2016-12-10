package techlytik.techlytik;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.widget.Toast;

import android.os.Handler;
import java.util.logging.LogRecord;

import utils.BluetoothUtils;

/**
 * Created by alex on 2016-08-17.
 */
public class ScannerBTLE {

    private ConnectMonitorActivity connectMonitorActivity;
    private long scan;
    private int signalStr;

    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler handler;

    public ScannerBTLE(ConnectMonitorActivity cma, long scanPeriod, int signalStrength) {
        this.connectMonitorActivity = cma;
        this.scan = scanPeriod;
        this.signalStr = signalStrength;

        final BluetoothManager bluetoothManager =
                (BluetoothManager) connectMonitorActivity.getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();
        handler = new Handler();

    }

    public boolean isScanning() {
        return mScanning;
    }

    public void start() {
        if(!BluetoothUtils.checkBluetooth(bluetoothAdapter)) {
            BluetoothUtils.requestUserBluetooth(connectMonitorActivity);
            connectMonitorActivity.stopScan();
        } else {
            scanLeDevice(true);
        }
    }

    public void stop(){
        scanLeDevice(false);
    }

    private void scanLeDevice(final boolean enable) {
        if(enable && !mScanning) {
            BluetoothUtils.toast(connectMonitorActivity.getApplicationContext(), "Starting Scan");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    connectMonitorActivity.stopScan();
                }
            }, scan);
            mScanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    final int new_rssi = rssi;
                    if(rssi > signalStr) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                connectMonitorActivity.addDevice(device, new_rssi);
                            }
                        });
                    }
                }
            };
}
