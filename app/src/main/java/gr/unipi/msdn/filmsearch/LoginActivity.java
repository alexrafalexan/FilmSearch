package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageLogin;
    TextView register;
    EditText emailText, passwordText;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        imageLogin = (ImageView) findViewById(R.id.btlogin);
        register = (TextView) findViewById(R.id.textviewregister);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progresslogin);
        findViewById(R.id.btlogin).setOnClickListener(this);
        findViewById(R.id.textviewregister).setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Content", "onPause");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textviewregister:
                Intent iRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(iRegister);
                Log.i("Content", "Press Register");
                break;

            case R.id.btlogin:
                Log.i("Content", "Press Login");
                userLogin();
                break;
        }
    }


    private void userLogin() {
        String username = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (username.isEmpty()) {
            emailText.setError("Email is required");
            emailText.requestFocus();
            Log.i("ERROR", "EMAIL EMPTY");
            return;
        }

        if (Patterns.EMAIL_ADDRESS.matcher(username).matches() == false) {
            emailText.setError("Please enter a valid emailText");
            emailText.requestFocus();
            Log.i("ERROR", "Please enter a valid emailText");
            return;
        }

        if (password.isEmpty()) {
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            Log.i("ERROR", "Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "You are Loged in", Toast.LENGTH_SHORT).show();
                    Intent iMain = new Intent(LoginActivity.this, ProfilActivity.class);
                    getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(iMain);
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
