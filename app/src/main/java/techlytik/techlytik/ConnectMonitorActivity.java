package techlytik.techlytik;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.BLE_ListAdapter;

public class ConnectMonitorActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;

    private String username, state, name;
    private Button scan;
    private ListView deviceList;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private HashMap<String, BTLE_Device> BTDeviceHashMap;
    private BLE_ListAdapter adapter;

    private BroadcastReciever_BTState broadcastReciever_btState;
    private ScannerBTLE scannerBTLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_monitor);

        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getApplicationContext(), "BlueTooth LE not supported", Toast.LENGTH_LONG);
            finish();
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            username = bundle.getString("username");
            state = bundle.getString("name");
            name = bundle.getString("state");
        }

        broadcastReciever_btState = new BroadcastReciever_BTState(getApplicationContext());
        BTDeviceHashMap = new HashMap<>();
        scannerBTLE = new ScannerBTLE(this, 5000, -100);

        adapter = new BLE_ListAdapter(getApplicationContext(), BTDeviceHashMap);
        deviceList = (ListView)findViewById(R.id.connectList);
        deviceList.setAdapter(adapter);

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ConnectMonitorActivity.this, ConfigureMonitorActivity.class);
                i.putExtra("username", username);
                i.putExtra("state", "new");
                i.putExtra("name", " ");
                startActivity(i);
                finish();
            }
        });

        scan = (Button)findViewById(R.id.connectScanButton);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!scannerBTLE.isScanning()) {
                    Toast.makeText(getApplicationContext(), "Scanning for Devices", Toast.LENGTH_SHORT);
                    startScan();
                } else {
                    stopScan();
                }
            }
        });

    }

    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReciever_btState, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReciever_btState);
        stopScan();
    }

    protected void onPause() {
        super.onPause();
        stopScan();
    }

    public void addDevice(BluetoothDevice device, int new_rssi) {
        String address = device.getAddress();
        String name = device.getName();
        if(name == null) return;
        if(!name.equals("OCENTRAL")) return;
        else {
            if (!BTDeviceHashMap.containsKey(address)) {
                BTLE_Device btle_device = new BTLE_Device(device);
                btle_device.setRSSI(new_rssi);
                BTDeviceHashMap.put(address, btle_device);
            } else {
                BTDeviceHashMap.get(address).setRSSI(new_rssi);
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void startScan() {
        scan.setText("Scanning...");
        BTDeviceHashMap.clear();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
        scannerBTLE.start();
    }

    public void stopScan() {
        scan.setText("Scan for monitor");
        scannerBTLE.stop();
    }
}
