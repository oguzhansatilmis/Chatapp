package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profilGoster extends AppCompatActivity {

    TextView gidilenKullaniciAdi, gidilenKullaniciID, gidilenKullaniciBio, gonderiSayiInt;
    Button mesajAt, takipButon;
    String kullaniciIDal, kullaniciId, takipKey, myKullaniciAdi, myUserid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, timelinereference, takiprefence, bilgirefence, takipEdilenreference;
    RecyclerView digerkullaniciprofilgosterRecy;
    List<TimelineModel> list, takipList;
    List<String> takipEdilenListe;
    TimelineAdapter timelineAdapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_goster);
        tanimla();
        timelineListele();
        mesajAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilGoster.this, ChatActivity.class);
                intent.putExtra("digerKullaniciID", kullaniciIDal); // chat activitye putExtra ettim.
                startActivity(intent);
            }
        });
        takipButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takipIstekyolla();
            }
        });

        if(takipButon.getText().equals("Takiptesin"))
        {

        }

      // takipEdilen();
    }

    public void tanimla() {
        gidilenKullaniciAdi = findViewById(R.id.gidilenKullaniciAdi);
        gidilenKullaniciID = findViewById(R.id.gidilenKullaniciID);
        gidilenKullaniciBio = findViewById(R.id.gidilenKullaniciBio);
        gonderiSayiInt = findViewById(R.id.gonderiSayiInt);
        mesajAt = findViewById(R.id.mesajAt);
        takipButon = findViewById(R.id.takipButon);
        kullaniciIDal = getIntent().getExtras().getString("digerKullaniciID");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        takiprefence = firebaseDatabase.getReference();
        timelinereference = firebaseDatabase.getReference();
        bilgirefence = firebaseDatabase.getReference();
        takipEdilenreference = firebaseDatabase.getReference();
        digerkullaniciprofilgosterRecy = findViewById(R.id.digerkullaniciprofilgosterRecy);
        kullaniciListele();
        list = new ArrayList<>();
        takipList = new ArrayList<>();
        takipEdilenListe = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        myUserid = mAuth.getUid();
        digerkullaniciprofilgosterRecy = findViewById(R.id.digerkullaniciprofilgosterRecy);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(profilGoster.this, 1);
        digerkullaniciprofilgosterRecy.setLayoutManager(layoutManager);


        timelineAdapter = new TimelineAdapter(profilGoster.this, list, profilGoster.this, kullaniciIDal);
        digerkullaniciprofilgosterRecy.setAdapter(timelineAdapter);


        bilgirefence.child("Users").child(myUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myKullaniciAdi = snapshot.child("kullaniciID").getValue().toString();
                takipEdilen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void kullaniciListele() {
        reference.child("Users").child(kullaniciIDal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isim = snapshot.child("adin").getValue().toString();
                String biyografi = snapshot.child("bio").getValue().toString();
                kullaniciId = snapshot.child("kullaniciID").getValue().toString();
                gidilenKullaniciAdi.setText(isim);
                gidilenKullaniciID.setText(kullaniciId);
                gidilenKullaniciBio.setText(biyografi);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void timelineListele() {
        timelinereference.child("TimeLine").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                TimelineModel timeline = snapshot.getValue(TimelineModel.class);

                if (timeline.getFrom().equals(kullaniciId)) {
                    list.add(timeline);

                    // bu if sayesinde sadece kendi paylastıklarımız gozukuyor.
                    timelineAdapter.notifyDataSetChanged();

                    gonderiSayiInt.setText(String.valueOf(list.size()));

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

    public void takipIstekyolla() {

        takipKey = takiprefence.child("Takip").child("takipIstekleri").push().getKey();

        Map takipMap = new HashMap<>();
        takipMap.put("kimden", myKullaniciAdi);
        takipMap.put("kime", kullaniciId);


        takiprefence.child("Takip").child("takipIstekleri").child(takipKey).setValue(takipMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Takip isteği yollandı", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    public void takipEdilen() {
        takipEdilenreference.child("Takip").child("takipListesi").child(myKullaniciAdi).child("takipEdilen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String takipedilen = snapshot.getValue().toString();

                if(takipedilen.equals(kullaniciId))
                {
                    takipButon.setText("Takiptesin");
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
