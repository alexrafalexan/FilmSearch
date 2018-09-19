package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText emailText, passwordText1, passwordText2;
    TextView alreadyRegister;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailText = (EditText) findViewById(R.id.email);
        passwordText1 = (EditText) findViewById(R.id.password);
        passwordText2 = (EditText) findViewById(R.id.repassword);
        progressBar =(ProgressBar) findViewById(R.id.progressregister);
        alreadyRegister = (TextView) findViewById(R.id.alreadregister);
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.btlogin).setOnClickListener(this);
        findViewById(R.id.alreadregister).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void registerUser(){
        String username = emailText.getText().toString().trim();
        String password1 = passwordText1.getText().toString().trim();
        String password2 = passwordText2.getText().toString().trim();

        if(username.isEmpty()){
            emailText.setFocusableInTouchMode(true);
            emailText.requestFocus();
            emailText.setError("Email is required");
            Log.i("ERROR","EMAIL EMPTY");
            return;
        }

        if (Patterns.EMAIL_ADDRESS.matcher(username).matches() == false){
            emailText.setError("Please enter a valid e-mail");
            emailText.requestFocus();
            Log.i("ERROR","Please enter a valid e-mail");
            return;
        }

        if(password1.isEmpty()){
            passwordText1.setError("Password is required");
            passwordText1.requestFocus();
            Log.i("ERROR","Password is required");
            return;
        }

        if(password1.length() < 6 ){
            passwordText1.setError("Minimun lenght of password should be 6");
            passwordText1.requestFocus();
            Log.i("ERROR","Minimun lenght of password should be 6");
            return;
        }

        if(password1.equals(password2)==false || password2.isEmpty()) {
            passwordText2.setError("Password does not match");
            passwordText2.requestFocus();
            Log.i("ERROR", password1 + "  " + password2);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            mAuth.createUserWithEmailAndPassword(username, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User Registered Successfull", Toast.LENGTH_SHORT).show();
                        Intent iLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                        getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(iLogin);
                    }
                    else {
                        if(task.getException() instanceof FirebaseAuthActionCodeException){
                            Toast.makeText(RegisterActivity.this,"You already registered",Toast.LENGTH_SHORT).show();
                        }
                      else{
                            String s = "Sign up Failed" + task.getException();
                            Toast.makeText(RegisterActivity.this, s,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btlogin){
            Log.i("Content","Press Register");
            registerUser();
            return;
        }else {
            Intent iLogin = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(iLogin);
        }
    }
}
