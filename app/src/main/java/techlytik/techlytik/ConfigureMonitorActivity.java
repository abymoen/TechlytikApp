package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.BackgroundWorkerCompany;
import utils.BackgroundWorkerConfigure;
import utils.BackgroundWorkerReConfig;

public class ConfigureMonitorActivity extends AppCompatActivity {

    private String username = "";
    private Button submit;
    private EditText eName, eField, eRun, eSurfaceLocation, eDrumholeLocation;
    private String company;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String state, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_monitor);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            username = extra.getString("username");
            state = extra.getString("state");
            name = extra.getString("name");
        }

        BackgroundWorkerCompany backgroundWorkerCompany = new BackgroundWorkerCompany(getApplicationContext(), ConfigureMonitorActivity.this);
        backgroundWorkerCompany.execute(username);

        submit = (Button)findViewById(R.id.configSubmitButton);
        eName = (EditText)findViewById(R.id.configNameEdit);
        eField = (EditText)findViewById(R.id.configFieldEdit);
        eRun = (EditText)findViewById(R.id.configRunEdit);
        eSurfaceLocation = (EditText)findViewById(R.id.configSurfaceEdit);
        eDrumholeLocation = (EditText)findViewById(R.id.configDrumholeEdit);

        final String[] entries = new String[5];

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = eName.getText().toString().trim();
                String mField = eField.getText().toString().trim();
                String mRun = eRun.getText().toString().trim();
                String mSurface = eSurfaceLocation.getText().toString().trim();
                String mDrumhole = eDrumholeLocation.getText().toString().trim();

                entries[0] = mField;
                entries[1] = mRun;
                entries[2] = mSurface;
                entries[3] = mDrumhole;
                entries[4] = mName;
                boolean goodData = stringCheck(entries);

                if(goodData && state.equals("new")) {
                    BackgroundWorkerConfigure backgroundWorkerConfigure = new BackgroundWorkerConfigure(getApplicationContext(), ConfigureMonitorActivity.this);
                    backgroundWorkerConfigure.execute(username, mName, mField, mRun, mSurface, mDrumhole, company);
                } else if(goodData && state.equals("config")) {
                    BackgroundWorkerReConfig backgroundWorkerReConfig = new BackgroundWorkerReConfig(getApplicationContext(), ConfigureMonitorActivity.this);
                    backgroundWorkerReConfig.execute(username, mName, mField, mRun, mSurface, mDrumhole, company, name);
                }
                else {

                }
            }
        });
    }


    public boolean stringCheck(String[] data) {
        for(int i=0; i<data.length; i++) {
            if(data[i].length() <= 0) {
                Toast.makeText(getApplicationContext(),"No Blank Entries Please", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void startNextActivity() {
        Intent i = new Intent(ConfigureMonitorActivity.this, ConfirmConfigurActivity.class);
        i.putExtra("username",username);
        startActivity(i);
        finish();
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
}
