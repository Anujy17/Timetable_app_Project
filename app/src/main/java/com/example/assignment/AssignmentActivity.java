package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.assignment.Adapter.ToDoAdapter;
import com.example.assignment.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AssignmentActivity extends AppCompatActivity implements OnDialogCloseListner{
    RecyclerView recyclerView;
    ImageView mFab,back;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    FirebaseAuth mAuth;
    String User;
    private Query query;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_assignment);
        recyclerView = findViewById(R.id.recyclerView);
        mFab= findViewById(R.id.add);
        mAuth= FirebaseAuth.getInstance();
        back= findViewById(R.id.back);
        firestore= FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AssignmentActivity.this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
        mList = new ArrayList<>();
        adapter=new ToDoAdapter(AssignmentActivity.this,mList);
        recyclerView.setAdapter(adapter);
        showData();
    }
    private void showData(){
        User = mAuth.getCurrentUser().getUid();
           query=firestore.collection("task").document(User).collection("Task Data").orderBy("due", Query.Direction.DESCENDING);
                   listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
               @Override
               public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                   for(DocumentChange documentChange : value.getDocumentChanges()){
                       if(documentChange.getType() == DocumentChange.Type.ADDED){
                           String id = documentChange.getDocument().getId();
                           ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                           mList.add(toDoModel);
                           adapter.notifyDataSetChanged();
                       }
                   }
                   listenerRegistration.remove();
               }
           });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}