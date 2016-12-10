package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import data.CustomAdapter;
import utils.BackgroundWorkerCompany;
import utils.BackgroundWorkerSpinner;
import utils.BackgroundWorkerSpinnerSelect;

public class MonitorListActivity extends AppCompatActivity {

    private ListView list;
    private CustomAdapter customAdapter;
    private String username = "";
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TextView totalText;
    private Spinner runOptions;
    private String company, run;
    private String[] runArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_list);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            username = extra.getString("username");
        }
        BackgroundWorkerCompany backgroundWorkerCompany = new BackgroundWorkerCompany(getApplicationContext(), MonitorListActivity.this);
        backgroundWorkerCompany.execute(username);
    }

    public void parseRuns(String data) {
        if(data == null) {
            Toast.makeText(getApplicationContext(),"Failed to Connect",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("server_response");

            int count = 0;
            HashMap<String, String> runmap = new HashMap<>();

            while(count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                run = JO.getString("run");
                if(runmap.containsKey(run)) {
                    count++;
                    continue;
                } else {
                    runmap.put(run, run);
                    count++;
                    continue;
                }
            }

            runArray = new String[runmap.size() + 1];
            int i=1;
            runArray[0] = "All Company Monitors";
            for(String key : runmap.keySet()) {
                runArray[i] = key;
                i++;
            }

        } catch (JSONException e){

        }
    }

    public void parseCompany(String data) {
        if(data == null) {
            Toast.makeText(getApplicationContext(), "Failed to Connect",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject JO = jsonArray.getJSONObject(0);
            company = JO.getString("company");

        } catch(JSONException e) {
            e.printStackTrace();
        }
    }



    public void parseJSON(String data) {
        if(data == null) {
            Toast.makeText(getApplicationContext(),"Failed to Connect",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("server_response");
            totalText = (TextView)findViewById(R.id.totalMonitorText);
            int count = 0;
            String status, name, location, arm;
            ArrayList<HashMap<String, String>> statusList = new ArrayList<>();
            totalText.setText("Total Monitors: " + jsonArray.length());

            while(count<jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("name");
                status = JO.getString("status");
                location = JO.getString("location");
                arm = JO.getString("armed");
                HashMap<String, String> datamap = new HashMap<>();
                datamap.put("status",status);
                datamap.put("id",name);
                datamap.put("location",location);
                datamap.put("armed", arm);
                statusList.add(datamap);
                count++;
            }

            list=(ListView)findViewById(R.id.monitorList);

            customAdapter = new CustomAdapter(getApplicationContext(), statusList);
            list.setAdapter(customAdapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MonitorListActivity.this, MonitorDetailsActivity.class);
                    TextView nameText = (TextView)view.findViewById(R.id.monitorIdText);
                    String name = nameText.getText().toString().trim();
                    i.putExtra("name",name);
                    i.putExtra("username", username);
                    i.putExtra("company", company);
                    startActivity(i);
                    finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void nextParse() {
        BackgroundWorkerSpinner backgroundWorkerSpinner = new BackgroundWorkerSpinner(getApplicationContext(), this);
        backgroundWorkerSpinner.execute(company);
    }

    public void populateSpinner() {
        runOptions = (Spinner)findViewById(R.id.listSpinner);
        runOptions.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, runArray));

        runOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                if(selection.equals("All Company Monitors")) {
                    selection = "all";
                }

                BackgroundWorkerSpinnerSelect backgroundWorkerSpinnerSelect = new BackgroundWorkerSpinnerSelect(getApplicationContext(), MonitorListActivity.this);
                backgroundWorkerSpinnerSelect.execute(company, selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
