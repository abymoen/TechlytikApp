package techlytik.techlytik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import utils.BackgroundWorkerRecover;

public class RecoverPasswordActivity extends AppCompatActivity {

    private Button submit;
    private EditText comp, user;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        submit = (Button)findViewById(R.id.forgotSubmitButton);
        comp = (EditText)findViewById(R.id.forgotCompanyEdit);
        user = (EditText)findViewById(R.id.forgotEmailEdit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = user.getText().toString().trim();
                String company = comp.getText().toString().trim();

                BackgroundWorkerRecover backgroundWorkerRecover = new BackgroundWorkerRecover(getApplicationContext(), RecoverPasswordActivity.this);
                backgroundWorkerRecover.execute(username, company);

            }
        });
    }

    public void startChangeActivity(String ver) {
        if(ver.equals("User success")) {
            Intent i = new Intent(RecoverPasswordActivity.this, ChangePasswordActivity.class);
            i.putExtra("username", username);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        } else if(ver.equals("User failure")) {
            Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_SHORT).show();
        } else if (ver.equals("Company Invalid")) {
            Toast.makeText(getApplicationContext(), "Invalid company", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
        }
    }

}
