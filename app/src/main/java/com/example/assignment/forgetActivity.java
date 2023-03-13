package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

public class forgetActivity extends AppCompatActivity {
    MaterialButton button;
    TextInputLayout email;
    TextView forg;
    FirebaseAuth auth;
    String ema;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);

        forg=findViewById(R.id.login_f);
        forg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });
        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        ema = email.getEditText().getText().toString();
        if(!ema.isEmpty()){
            forgetPassword();
        }else{
            email.setError("Please enter email");
        }
    }

    private void forgetPassword() {
        auth.sendPasswordResetEmail(ema).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forgetActivity.this,"Please check your mail",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(forgetActivity.this,LoginActivity.class));
                    finish();

                }else{
                    Toast.makeText(forgetActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}