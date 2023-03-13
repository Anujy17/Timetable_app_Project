package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.security.auth.login.LoginException;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton assign,tasks;
    ImageButton img;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    CardView Monday,Tuesday,Wed,thu,fri,sat,sun;
    public TextView user_t;

    MaterialButton task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        task = findViewById(R.id.task);
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AssignmentActivity.class));
            }
        });
        user_t=findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            String userid=user.getUid();
            DocumentReference reference;
            FirebaseFirestore fire = FirebaseFirestore.getInstance();
            reference = fire.collection("users").document(userid);
            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String Name1 = task.getResult().getString("full_name");
                    String firstWord = Name1;
                    String Name=" ";
                    if(firstWord.contains(" ")){
                        firstWord = firstWord.substring(0,firstWord.indexOf(" "));
                        Name = firstWord;
                    }
                    else{
                        Name=firstWord;
                    }
                    user_t.setText("Hello "+Name);

                }
            });
        }
        else{
            user_t.setText("Please Login");
        }

        img =findViewById(R.id.profile);
        Tuesday=findViewById(R.id.Tuesday);
        Tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TuesdayActivity.class));
            }
        });

        Wed = findViewById(R.id.wednesday);
        Wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),WednesdayActivity.class));
            }
        });
        thu=findViewById(R.id.thursday);
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ThursdayActivity.class));
            }
        });
        fri =findViewById(R.id.friday);
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FridayActivity.class));
            }
        });
        sat=findViewById(R.id.saturday);
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SaturdayActivity.class));
            }
        });
        sun = findViewById(R.id.sunday);
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SundayActivity.class));
            }
        });
        Monday = findViewById(R.id.monday);
        Monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MondayActivity.class);
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                if(mUser==null){
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }
                else{
                    startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
                }

            }
        });
    }
}