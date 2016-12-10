package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import utils.BackgroundWorkerRegister;

public class NewUserActivity extends AppCompatActivity {

    private Button submit;
    private EditText mPasswordEdit, mRepeatEdit, mEmailEdit, mCompanyEdit;
    private boolean goodpass, gooduser, matchpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        submit = (Button)findViewById(R.id.newSubmitButton);
        mPasswordEdit = (EditText)findViewById(R.id.newPassword);
        mRepeatEdit = (EditText)findViewById(R.id.newConfirmPassword);
        mEmailEdit = (EditText)findViewById(R.id.newEmail);
        mCompanyEdit = (EditText)findViewById(R.id.newCompanyName);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mEmailEdit.getText().toString().trim();
                String password = mPasswordEdit.getText().toString().trim();
                String passwordRepeat = mRepeatEdit.getText().toString().trim();
                String company = mCompanyEdit.getText().toString().trim();


                goodpass=passwordChecker(password);
                gooduser=usernameChecker(username);
                matchpass = passwordMatch(password, passwordRepeat);

                if(matchpass && goodpass && gooduser) {
                    BackgroundWorkerRegister backgroundWorkerRegister = new BackgroundWorkerRegister(getApplicationContext(), NewUserActivity.this);
                    backgroundWorkerRegister.execute(username, password, company);
                }
            }
        });
    }

    public void registerActivity(String result) {
        if(result.equals("Register success")) {
            Intent i = new Intent(NewUserActivity.this, ComfirmRegisterActivity.class);
            startActivity(i);
            finish();
        } else if(result.equals("Email copy")) {
            Toast.makeText(getApplicationContext(),"Username Exists",Toast.LENGTH_SHORT).show();
        } else if (result.equals(("Company Invalid"))){
            Toast.makeText(getApplicationContext(),"Invalid Company Name",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Unexpected Error",Toast.LENGTH_SHORT).show();
        }
    }


    public boolean passwordChecker(String password) {
        int len = password.length();
        if(len < 6 || len > 30) {
            Toast.makeText(getApplicationContext(),"Password between 6-30 Characters", Toast.LENGTH_LONG).show();
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

    public boolean usernameChecker(String username) {
        int len = username.length();
        if(len < 6 || len > 30) {
            Toast.makeText(getApplicationContext(),"Username between 6-30 Characters", Toast.LENGTH_LONG).show();
            return false;
        } else{
            return true;
        }
    }
}
