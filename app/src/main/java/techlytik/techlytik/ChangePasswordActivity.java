package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import utils.BackgroundWorkerChange;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button submit;
    private String user;
    private EditText pass, passrepeat;
    private boolean goodpass, goodpasslength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            user = extra.getString("username");
        }

        Log.v("PASSEDNAME", user);

        pass= (EditText)findViewById(R.id.changePasswordEdit);
        passrepeat = (EditText)findViewById(R.id.changeRepeatEdit);

        submit = (Button)findViewById(R.id.changeSubmitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = pass.getText().toString().trim();
                String passwordRepeat = passrepeat.getText().toString().trim();

                goodpasslength = passwordChecker(password);
                goodpass = passwordMatch(password, passwordRepeat);

                if(goodpass && goodpasslength) {
                    BackgroundWorkerChange backgroundWorkerChange = new BackgroundWorkerChange(getApplicationContext(), ChangePasswordActivity.this);
                    backgroundWorkerChange.execute(user, password);
                }
            }
        });
    }


    public boolean passwordChecker(String password) {
        int len = password.length();
        if(len < 6 || len > 30) {
            Toast.makeText(getApplicationContext(), "Password between 6-30 Characters", Toast.LENGTH_LONG).show();
            return false;
        } else{
            return true;
        }
    }

    public boolean passwordMatch(String password, String repeat) {
        if(password.equals(repeat)) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(),"Passwords Don't Match", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void startNextActivity(String ver) {
        if(ver.equals("Password success")) {
            Intent i = new Intent(ChangePasswordActivity.this, ChangeSuccessActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_SHORT).show();
        }
    }
}
