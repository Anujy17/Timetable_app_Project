package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TimeActivity extends AppCompatActivity {
    TextView t1,t2,detail;
    public static final String TAG = "TAG";
    int t1hour,t1minute,t2hour,t2minute;
    MaterialButton save_btn;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String User,Doc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_time);
        EditText Title = findViewById(R.id.title);
        EditText Desc = findViewById(R.id.description);
        detail = findViewById(R.id.detail);
        intent = getIntent();
        Doc = intent.getStringExtra("class");
        t1=findViewById(R.id.start);
        progressDialog = new ProgressDialog(this);
        t2=findViewById(R.id.end);
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        TimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t1hour = i;
                                t1minute = i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t1hour,t1minute);
                                t1.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(t1hour,t1minute);
                timePickerDialog.show();
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        TimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2hour=i;
                                t2minute=i1;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t2hour,t2minute);
                                t2.setText(DateFormat.format("hh:mm aa",calendar));
                            }
                        },12,0,false
                );
                timePickerDialog.updateTime(t2hour,t2minute);
                timePickerDialog.show();
            }
        });
        save_btn=findViewById(R.id.save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please wait while saving...");
                progressDialog.setTitle("saving....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                Map<String, Object> subject = new HashMap<>();
                subject.put("Title",""+Title.getText().toString() );
                subject.put("start",""+t1.getText().toString() );
                subject.put("end", ""+t2.getText().toString());
                subject.put("Desc", ""+Desc.getText().toString());
                User = mAuth.getCurrentUser().getUid();
                db.collection("subjects").document(User).collection(Doc)
                        .add(subject)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getApplicationContext(),"not",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}