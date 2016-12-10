package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserSettingActivity extends AppCompatActivity {


    private Button changePassword;
    private String username= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        changePassword = (Button)findViewById(R.id.settingsPasswordButton);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            username = extra.getString("username");
        }



        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(UserSettingActivity.this, ChangePasswordActivity.class);
                i.putExtra("username", username);
                startActivity(i);
                finish();
            }
        });
    }
}
