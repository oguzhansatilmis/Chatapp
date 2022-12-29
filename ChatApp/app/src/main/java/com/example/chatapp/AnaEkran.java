package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnaEkran extends AppCompatActivity {

    ImageView homepage,bildirim,friends,forum;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,reference,takipEdilenreference;
    String userid,kullaniciID;
    List<TimelineModel> list;
    List<String> takipEdilenListe;
    RecyclerView anaEkranRecylerView;
    TimelineAdapter timelineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);
        tanimla();
        kullaniciBilgileriniGetir();

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaEkran.this,HomePageActivity.class);
                intent.putExtra("kullaniciID",kullaniciID);
                startActivity(intent);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaEkran.this,kullaniciEkran.class);
                startActivity(intent);
            }
        });

        bildirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaEkran.this, TakipActivity.class);
                intent.putExtra("kullaniciID",kullaniciID);
                startActivity(intent);
            }
        });
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnaEkran.this,ForumActivity.class);
                intent.putExtra("kullaniciID",kullaniciID);
                startActivity(intent);
            }
        });



    }

    public void tanimla()
    {
        homepage = findViewById(R.id.homepage);
        bildirim = findViewById(R.id.bildirim);
        friends = findViewById(R.id.friends);
        forum = findViewById(R.id.forum);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        takipEdilenreference = firebaseDatabase.getReference();

        anaEkranRecylerView = findViewById(R.id.anaEkranRecylerView);
        list = new ArrayList<>();
        takipEdilenListe =new ArrayList<>();


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AnaEkran.this,1);
        anaEkranRecylerView.setLayoutManager(layoutManager);


        timelineAdapter = new TimelineAdapter(AnaEkran.this,list, AnaEkran.this,"kullaniciID");
        anaEkranRecylerView.setAdapter(timelineAdapter);


    }
    public void kullaniciBilgileriniGetir()
    {
        userid = firebaseAuth.getUid();

        databaseReference.child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kullaniciID = snapshot.child("kullaniciID").getValue().toString();
                takipEdilen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void loadTimeline(String takip)
    {
        reference.child("TimeLine").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                TimelineModel timeline = snapshot.getValue(TimelineModel.class);
                if(timeline.getFrom().equals(takip)) {
                    list.add(timeline);
                    timelineAdapter.notifyDataSetChanged();
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
    public void takipEdilen()
    {
        takipEdilenreference.child("Takip").child("takipListesi").child(kullaniciID).child("takipEdilen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String takipEdilenler = snapshot.getValue().toString();
                loadTimeline(takipEdilenler);


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