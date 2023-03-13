package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    TextView forg,reg;
    MaterialButton Log;
    TextInputLayout ema,pass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        forg=findViewById(R.id.forget);
        reg=findViewById(R.id.register);
        forg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),forgetActivity.class);
                startActivity(in);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(in);
            }
        });

        //assign the variables
        ema = findViewById(R.id.email);
        pass=findViewById(R.id.password);
        Log = findViewById(R.id.Login);
        reg=findViewById(R.id.register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

       Log.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Login();
           }
       });

    }

    private void Login() {

        String mail = ema.getEditText().getText().toString();
        String password = pass.getEditText().getText().toString();
        if(!mail.matches(emailPattern)){
            ema.setError("Please enter correct email");
        }else if(password.isEmpty()||password.length()<6){
            pass.setError("Please enter proper password");
        }else {
            progressDialog.setMessage("Please wait while Login...");
            progressDialog.setTitle("Login....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        SendUserToNextActivity();
                        Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void SendUserToNextActivity() {
        Intent in = new Intent(LoginActivity.this,HomeActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

}