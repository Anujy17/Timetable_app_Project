package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
    //declaring variables
    public static final String TAG = "TAG";
    TextInputLayout name_t,email_t,phone_t,password_t;
    MaterialButton reg;
    TextView register,sign_in;
    FirebaseAuth mAuth;
    String userID;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);
        name_t = findViewById(R.id.name);
        phone_t=findViewById(R.id.mobile);
        email_t = findViewById(R.id.email);
        sign_in = findViewById(R.id.sign);
        password_t = findViewById(R.id.password);
        reg=findViewById(R.id.Login);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    private void PerformAuth() {
        String mail = email_t.getEditText().getText().toString();
        String password = password_t.getEditText().getText().toString();
        String full = name_t.getEditText().getText().toString();
        String pho = phone_t.getEditText().getText().toString();
        if(!mail.matches(emailPattern)){
            email_t.setError("Please enter correct email");
        }else if(password.isEmpty()||password.length()<6){
            password_t.setError("Please enter proper password");
        }else{
            progressDialog.setMessage("Please wait while Registration...");
            progressDialog.setTitle("Registration....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("full_name", full);
                        user.put("email", mail);
                        user.put("phone", pho);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user profile is created for " + userID);
                            }
                        });
                        SendUserToNextActivity();
                        Toast.makeText(RegistrationActivity.this,"Registration Successfull",Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        switch (errorCode) {

                            case "ERROR_INVALID_CUSTOM_TOKEN":
                                Toast.makeText(RegistrationActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                Toast.makeText(RegistrationActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_CREDENTIAL":
                                Toast.makeText(RegistrationActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_EMAIL":
                                Toast.makeText(RegistrationActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                email_t.setError("The email address is badly formatted.");
                                email_t.requestFocus();
                                break;
                            case "ERROR_WRONG_PASSWORD":
                                Toast.makeText(RegistrationActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                password_t.setError("password is incorrect ");
                                password_t.requestFocus();
                                break;

                            case "ERROR_USER_MISMATCH":
                                Toast.makeText(RegistrationActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_REQUIRES_RECENT_LOGIN":
                                Toast.makeText(RegistrationActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                Toast.makeText(RegistrationActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                Toast.makeText(RegistrationActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                email_t.setError("The email address is already in use by another account.");
                                email_t.requestFocus();
                                break;

                            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                Toast.makeText(RegistrationActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_DISABLED":
                                Toast.makeText(RegistrationActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_TOKEN_EXPIRED":
                                Toast.makeText(RegistrationActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_USER_NOT_FOUND":
                                Toast.makeText(RegistrationActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_INVALID_USER_TOKEN":
                                Toast.makeText(RegistrationActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_OPERATION_NOT_ALLOWED":
                                Toast.makeText(RegistrationActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                break;

                            case "ERROR_WEAK_PASSWORD":
                                Toast.makeText(RegistrationActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                password_t.setError("The password is invalid it must 6 characters at least");
                                password_t.requestFocus();
                                break;

                        }
                    }

                }
            });
        }
    }

    private void SendUserToNextActivity() {
        Intent in = new Intent(RegistrationActivity.this,LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }
}