package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TakipActivity extends AppCompatActivity {

    RecyclerView takipRecylerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,databaseReference;
    List<String> list;
    String kullaniciID,kimdenTakip;
    TakipAdapter takipAdapter;
    TextView bilgilendirme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takip);
       tanimla();
        listele();


    }
    public void tanimla()
    {
        takipRecylerView = findViewById(R.id.takipRecylerView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        bilgilendirme = findViewById(R.id.bilgilendirme);
        kullaniciID =  getIntent().getExtras().getString("kullaniciID");
        list = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TakipActivity.this,1);
        takipRecylerView.setLayoutManager(layoutManager);
        takipAdapter = new TakipAdapter(TakipActivity.this,list, TakipActivity.this,kullaniciID);
        takipRecylerView.setAdapter(takipAdapter);

    }
    public void listele()
    {
        reference.child("Takip").child("takipIstekleri").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    if(snapshot.child("kime").getValue().toString().equals(kullaniciID)) {
                        kimdenTakip = snapshot.child("kimden").getValue().toString();
                        list.add(kimdenTakip);
                        takipAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}