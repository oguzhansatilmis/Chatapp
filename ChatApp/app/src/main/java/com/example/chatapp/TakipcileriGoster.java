package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TakipcileriGoster extends AppCompatActivity {
    String kullaniciID;
    List<String> takipciListe;
    RecyclerView takipciRecylerView;
    TakipGosterAdapter adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference takipciReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takipcileri_goster);
        tanimla();
        takipci();
    }
    public  void tanimla()
    {
        takipciListe = new ArrayList<>();
        kullaniciID =  getIntent().getExtras().getString("kullaniciID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        takipciReference = firebaseDatabase.getReference();
        takipciRecylerView = findViewById(R.id.takipciRecylerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TakipcileriGoster.this);
        takipciRecylerView.setLayoutManager(layoutManager);
        adapter = new TakipGosterAdapter(TakipcileriGoster.this,takipciListe, TakipcileriGoster.this);
        takipciRecylerView.setAdapter(adapter);

    }
    public void takipci()
    {
        takipciReference.child("Takip").child("takipListesi").child(kullaniciID).child("takipci").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String takipciler = snapshot.getValue().toString();
                takipciListe.add(takipciler);
                adapter.notifyDataSetChanged();

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