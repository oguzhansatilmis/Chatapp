package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TakipEdilenleriGoster extends AppCompatActivity
{
    String kullaniciID;

    List<String> takipEdilenListe;
    RecyclerView takipEdilenRecylerView;
    TakipGosterAdapter takipGosterAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference takipEdilenreference,takipciReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takip_edilenleri_goster);
        tanimla();
         takipEdilen();

    }
    public  void tanimla()
    {
        takipEdilenListe = new ArrayList<>();
        kullaniciID =  getIntent().getExtras().getString("kullaniciID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        takipciReference = firebaseDatabase.getReference();
        takipEdilenreference = firebaseDatabase.getReference();
        takipEdilenRecylerView = findViewById(R.id.takipEdilenRecylerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TakipEdilenleriGoster.this);
        takipEdilenRecylerView.setLayoutManager(layoutManager);
        takipGosterAdapter = new TakipGosterAdapter(TakipEdilenleriGoster.this,takipEdilenListe, TakipEdilenleriGoster.this);
        takipEdilenRecylerView.setAdapter(takipGosterAdapter);

    }
    public void takipEdilen()
    {
        takipEdilenreference.child("Takip").child("takipListesi").child(kullaniciID).child("takipEdilen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String takipEdilenler = snapshot.getValue().toString();
                takipEdilenListe.add(takipEdilenler);
                takipGosterAdapter.notifyDataSetChanged();
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