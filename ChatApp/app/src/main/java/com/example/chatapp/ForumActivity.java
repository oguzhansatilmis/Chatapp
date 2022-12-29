package com.example.chatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumActivity extends AppCompatActivity
{
    FirebaseDatabase mDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    EditText forumKonuAcEditText;
    Button forumKonuAcButon;
    RecyclerView forumRecylerView;
    String digerkullaniciID;
    List<MesajModel> list;

    String konuBasligiKey;

   ForumAdapter forumAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        tanimla();
        loadForumBaslik();

        forumKonuAcButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String konubasligiText = forumKonuAcEditText.getText().toString();
                ForumKonuKaydet(konubasligiText);
                forumKonuAcEditText.setText("");
            }
        });
    }
    public void tanimla()
    {
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        forumKonuAcEditText = findViewById(R.id.forumKonuAcEditText);
        forumKonuAcButon = findViewById(R.id.forumKonuAcButon);
        forumRecylerView = findViewById(R.id.forumRecylerView);

        digerkullaniciID =  getIntent().getExtras().getString("kullaniciID"); // kendi Idmizi Ana ekrandan putExtra ile aldık


        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ForumActivity.this,1);
        forumRecylerView.setLayoutManager(layoutManager);


        forumAdapter = new ForumAdapter(ForumActivity.this,list, ForumActivity.this,digerkullaniciID);
        forumRecylerView.setAdapter(forumAdapter);

    }
    public void ForumKonuKaydet(String baslik)
    {
        konuBasligiKey = reference.child("Forum").child("Başlıklar").push().getKey();

        Map forumMap = new HashMap<>();
        forumMap.put("from",digerkullaniciID);
        forumMap.put("text",baslik);
        forumMap.put("tarih",gunTarih());

        reference.child("Forum").child("Başlıklar").child(konuBasligiKey).setValue(forumMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                    Toast.makeText(getApplicationContext(),"Konu Başarılı bir şekilde açıldı.",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public String gunTarih()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy");
        Date date = new Date();
        String guntarih = dateFormat.format(date);

        return guntarih;
    }
    public String saatTarih()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String saattarih = dateFormat.format(date);

        return saattarih;
    }


    public void loadForumBaslik()
    {
        reference.child("Forum").child("Başlıklar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                MesajModel forumbilgileri = snapshot.getValue(MesajModel.class);
                list.add(forumbilgileri);
                forumAdapter.notifyDataSetChanged();

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