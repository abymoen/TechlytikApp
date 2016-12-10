package techlytik.techlytik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.BackgroundWorkerArm;
import utils.BackgroundWorkerDelete;
import utils.BackgroundWorkerDetails;

public class MonitorDetailsActivity extends AppCompatActivity {

    private String name, username= "";
    private String company;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private TextView statusText, nameText, surfaceLocationText, drumholeLocationText,
                    fieldText, vibrationText, armedText, runText, deviceAddText, IPAddText, IPMaskText, IPGatewayText, RPMText;
    private String status, surfacelocation, drumholelocation, field, vibration, armed, armedState, run, deviceAdd, IPAdd, IPMask, IPGate, rpm;
    private ImageView statusImage;
    private Button delete, configure, arm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_details);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            name = extras.getString("name");
            username = extras.getString("username");
            company = extras.getString("company");
        }

        BackgroundWorkerDetails backgroundWorkerDetails = new BackgroundWorkerDetails(getApplicationContext(), this);
        backgroundWorkerDetails.execute(company, name);

        delete = (Button)findViewById(R.id.detailsDeleteButton);
        arm = (Button)findViewById(R.id.detailsArmButton);
        configure = (Button)findViewById(R.id.detailsConfigureButton);

        arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MonitorDetailsActivity.this);
                builder.setTitle("Arm Monitor");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackgroundWorkerArm backgroundWorkerArm = new BackgroundWorkerArm(getApplicationContext(), MonitorDetailsActivity.this);
                                if(armed.equals("yes")) {
                                    armedState = "no";
                                } else {
                                    armedState = "yes";
                                }
                                backgroundWorkerArm.execute(armedState, username, name);
                            }
                        });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MonitorDetailsActivity.this);
                builder.setTitle("Delete Monitor");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackgroundWorkerDelete backgroundWorkerDelete = new BackgroundWorkerDelete(getApplicationContext(), MonitorDetailsActivity.this);
                                backgroundWorkerDelete.execute(company, name);
                            }
                        });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MonitorDetailsActivity.this, ConfigureMonitorActivity.class);
                i.putExtra("username", username);
                i.putExtra("state", "config");
                i.putExtra("name", name);
                startActivity(i);
                finish();
            }
        });
    }

    public void parseJSON(String data) {

        statusText = (TextView)findViewById(R.id.detailsStatus);
        nameText = (TextView)findViewById(R.id.detailsName);
        vibrationText = (TextView)findViewById(R.id.detailsVibration);
        statusImage = (ImageView)findViewById(R.id.detailsStatusImage);
        armedText = (TextView)findViewById(R.id.detailsArmed);
        surfaceLocationText = (TextView)findViewById(R.id.detailsSurfaceLocation);
        drumholeLocationText = (TextView)findViewById(R.id.detailsDrumholeLocation);
        fieldText = (TextView)findViewById(R.id.detailsField);
        runText = (TextView)findViewById(R.id.detailsRun);
        RPMText =(TextView)findViewById(R.id.detailsRPM);

        if(data == null) {
            Toast.makeText(getApplicationContext(), "Failed to Connect", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;

            while(count < jsonArray.length()) {
                JSONObject JO = jsonArray.getJSONObject(count);
                status = JO.getString("status");
                vibration = JO.getString("vibration");
                armed = JO.getString("armed");
                surfacelocation = JO.getString("surfacelocation");
                drumholelocation = JO.getString("drumholelocation");
                field = JO.getString("field");
                run = JO.getString("run");
                name = JO.getString("name");
                rpm = JO.getString("RPM");

                statusText.setText("Status: " + status);
                nameText.setText("Monitor Name: " + name);
                vibrationText.setText("Vibrations: " + vibration);
                armedText.setText("Armed: " + armed);
                RPMText.setText("RPM: " + rpm);
                drumholeLocationText.setText("Downhole Location: " + drumholelocation);
                surfaceLocationText.setText("Surface Location: " + surfacelocation);
                runText.setText("Run: " + run);
                fieldText.setText("Field: " + field);

                if(armed.equals("no")) {
                    statusImage.setImageResource(R.drawable.black);
                    arm.setText("Enable alarms");
                } else if(status.equals("optimal") && armed.equals("yes")){
                    statusImage.setImageResource(R.drawable.green);
                    arm.setText("Disable alarms");
                } else {
                    statusImage.setImageResource(R.drawable.red);
                    arm.setText("Disable alarms");
                }
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startNextActivity() {
        Toast.makeText(getApplicationContext(), "Monitor Deleted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MonitorDetailsActivity.this, UserLandingActivity.class);
        i.putExtra("username", username);
        startActivity(i);
        finish();
    }

    public void refreshPage() {
        BackgroundWorkerDetails backgroundWorkerDetails = new BackgroundWorkerDetails(getApplicationContext(), this);
        backgroundWorkerDetails.execute(username, name);
    }
}
