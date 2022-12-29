package com.example.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class kullaniciEkran extends AppCompatActivity {

    List<String> kullaniciAdiListe,kullaniciIDListe;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;

    RecyclerView userRecyView;
    UserAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_ekran);
        tanimla();
        listele();
    }

    public void tanimla()
    {
        kullaniciAdiListe = new ArrayList<>();
        kullaniciIDListe = new ArrayList<>();
        firebaseDatabase = firebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        userRecyView = findViewById(R.id.recyc_kullanicilar);


        String user_id = auth.getCurrentUser().getUid();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(kullaniciEkran.this);
        userRecyView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(kullaniciEkran.this,kullaniciAdiListe,kullaniciIDListe, kullaniciEkran.this,user_id);
        userRecyView.setAdapter(userAdapter);
    }

    public void listele()
    {
        String user_id = auth.getCurrentUser().getUid();
        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               if(!snapshot.getKey().equals(user_id))
               {   String kullaniciIDleri = snapshot.getKey();
                   kullaniciIDListe.add(kullaniciIDleri);   // kullanici idlerini ayrı bir listeye aldım.
                   String kullaniciAdlari = snapshot.child("kullaniciID").getValue().toString(); //userIDlerin cocugu olan "kullaniciId" yi aldık.
                   kullaniciAdiListe.add(kullaniciAdlari); // listeye "kullaniciID" leri ekledik.
                   userAdapter.notifyDataSetChanged();


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