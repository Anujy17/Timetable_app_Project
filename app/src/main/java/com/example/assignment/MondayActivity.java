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
import android.widget.Toast;

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

public class MondayActivity extends AppCompatActivity {
    ImageView b;
    RecyclerView recyclerView;
    FirebaseUser mUser;
    ArrayList<User> userArrayList;
    SubjectAdapter Subject;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userID;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monday);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        ImageView plus = findViewById(R.id.add);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userID = "subjects"+mAuth.getCurrentUser().getUid()+"Monday";
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<User>();
        Subject = new SubjectAdapter(MondayActivity.this,userArrayList);
        recyclerView.setAdapter(Subject);
        EventChangListener();
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MondayActivity.this, TimeActivity.class);
                intent.putExtra("class", "Monday");
                startActivity(intent);
            }
        });
        b = findViewById(R.id.back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });


    }

    private void EventChangListener() {
        db.collection("subjects").document(mAuth.getCurrentUser().getUid()).collection("Monday").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
 db.collection("subjects").document(mAuth.getCurrentUser().getUid()).collection("Monday").addSnapshotListener(new
                                           EventListener<QuerySnapshot>() {
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