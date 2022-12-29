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
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumYorumYap extends AppCompatActivity {

    Button forumKonuBasligi;
    String KonuBaslik,kullaniciID,userId,userKullanici;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference2;
    EditText forumYorumYapEditText;
    Button forumYorumYapEditButon;
    List<MesajModel> list;
    RecyclerView forumYorumYazReyclerView;
    FirebaseAuth mAuth;

    ForumYorumAdapter forumYorumAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_yorum_yap);
        tanimla();
        loadForumBaslik();

        forumYorumYapEditButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yorum = forumYorumYapEditText.getText().toString();
                yorumYap(yorum);
                forumYorumYapEditText.setText("");
            }
        });

    }
    public void tanimla()
    {
        forumKonuBasligi = findViewById(R.id.forumKonuBasligi);
        forumYorumYapEditText = findViewById(R.id.forumYorumYapEditText);
        forumYorumYapEditButon = findViewById(R.id.forumYorumYapEditButon);
        KonuBaslik =  getIntent().getExtras().getString("KonuBaslik");
        kullaniciID =  getIntent().getExtras().getString("kullaniciID");
        forumKonuBasligi.setText(KonuBaslik);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference2 = firebaseDatabase.getReference();

        mAuth= FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        databaseReference2.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userKullanici = snapshot.child("kullaniciID").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        list = new ArrayList<>();


        forumYorumYazReyclerView = findViewById(R.id.forumYorumYazReyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ForumYorumYap.this,1);
        forumYorumYazReyclerView.setLayoutManager(layoutManager);


        forumYorumAdapter = new ForumYorumAdapter(ForumYorumYap.this,list, ForumYorumYap.this,kullaniciID);
        forumYorumYazReyclerView.setAdapter(forumYorumAdapter);
    }

    public void yorumYap(String yorum)
    {
        String yorumkey = databaseReference.child("Forum").child("Yorumlar").push().getKey();

        Map forumYorumYap = new HashMap<>();
        forumYorumYap.put("from",userKullanici);
        forumYorumYap.put("text",yorum);
        forumYorumYap.put("tarih",gunTarih());


        databaseReference.child("Forum").child("Yorumlar").child(KonuBaslik).child(yorumkey).setValue(forumYorumYap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Yorum Başarılı bir şekilde açıldı.",Toast.LENGTH_SHORT).show();
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
    public void loadForumBaslik()
    {
        databaseReference.child("Forum").child("Yorumlar").child(KonuBaslik).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                MesajModel forumbilgileri = snapshot.getValue(MesajModel.class);
                list.add(forumbilgileri);
                forumYorumAdapter.notifyDataSetChanged();

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