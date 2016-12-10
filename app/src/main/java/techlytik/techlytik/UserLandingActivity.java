package techlytik.techlytik;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserLandingActivity extends AppCompatActivity {

    private Button monitors, configure, settings;
    private TextView welcome;
    private String username = "";
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing);

        welcome = (TextView)findViewById(R.id.landingWelcomeText);
        monitors = (Button)findViewById(R.id.landingMonitorsButton);
        settings = (Button)findViewById(R.id.landingSettingsButton);
        configure = (Button)findViewById(R.id.landingConfigureButton);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            username = extra.getString("username");
        }

        welcome.setText("Welcome " + username);


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserLandingActivity.this, UserSettingActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });

        monitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserLandingActivity.this, MonitorListActivity.class);
                i.putExtra("username", username);
                startActivity(i);
            }
        });

        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLandingActivity.this, ConnectMonitorActivity.class);
                i.putExtra("username", username);
                i.putExtra("state", "new");
                i.putExtra("name", " ");
                startActivity(i);
            }
        });
    }
}
