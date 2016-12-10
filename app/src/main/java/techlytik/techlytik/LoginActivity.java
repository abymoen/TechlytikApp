package techlytik.techlytik;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import utils.BackgroundWorker;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mPasswordEdit, mEmailEdit;
    private Button login, forgot, newUser;
    private String verify = "";
    private AlertDialog dialog;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.

        login = (Button)findViewById(R.id.signinbutton);
        forgot = (Button)findViewById(R.id.forgotButton);
        newUser = (Button)findViewById(R.id.registerButton);
        mPasswordEdit = (EditText)findViewById(R.id.passwordEdit);
        mEmailEdit = (EditText)findViewById(R.id.emailEdit);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mEmailEdit.getText().toString().trim();
                String pass = mPasswordEdit.getText().toString().trim();
                String type = "login";

                startDialog();
                BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext(), LoginActivity.this);
                backgroundWorker.execute(type, username, pass);

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(i);
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, NewUserActivity.class);
                startActivity(i);
            }
        });
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String ver) {
        verify = ver;
    }

    public void loginActivity(String ver) {
        dialog.dismiss();
        if(verify.equals("Login success")) {
            String username = mEmailEdit.getText().toString().trim();
            Intent i = new Intent(LoginActivity.this, UserLandingActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        } else {
            Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show();
            Log.v("LOGINDATA", ver);
        }
    }

    public void startDialog() {
        builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Connecting");
        builder.setMessage("Verifying Credentials");
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
}

