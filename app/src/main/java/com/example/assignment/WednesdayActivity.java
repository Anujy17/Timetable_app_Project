package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WednesdayActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseUser mUser;
    ArrayList<User> userArrayList;
    SubjectAdapter Subject;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wednesday);
        ImageView back=findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<User>();
        Subject = new SubjectAdapter(WednesdayActivity.this,userArrayList);
        recyclerView.setAdapter(Subject);
        EventChangListener();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        ImageView plus = findViewById(R.id.add);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TimeActivity.class);
                intent.putExtra("class","Wednesday");
                startActivity(intent);
            }
        });
    }

    private void EventChangListener() {
        db.collection("subjects").document(mAuth.getCurrentUser().getUid()).collection("Tuesday").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int count =0;
                    for(DocumentSnapshot document : task.getResult()){
                        count++;
                    }
                }
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            }

        });
        db.collection("subjects").document(mAuth.getCurrentUser().getUid()).collection("Tuesday").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    Log.e("Database error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        final boolean add =
                                userArrayList.add(dc.getDocument().toObject(User.class));
                    }
                    Subject.notifyDataSetChanged();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }
        });

    }

}