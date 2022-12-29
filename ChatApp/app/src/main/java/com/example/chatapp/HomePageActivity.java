package com.example.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity
{
    TextView id,isim,bio,gonderiSayiInt,takipEdilenSayiInt,takipciSayiInt,takipEdilenText,takipciText;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,timelineReference,reference,takipEdilenreference,takipciReference;
    ProgressDialog progressDialog;
    EditText gonderiPaylas;
    Button gonderiPaylasButon;
    String kullaniciID;
    List<String> takipEdilenListe,takipciListe;
    List<TimelineModel> list;
    RecyclerView timelineReyc;
    TimelineAdapter timelineAdapter;
    String gonderiKey;
    ImageView homepageSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tanimla();
        veriCek();
        loadTimeline();



        gonderiPaylasButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String gonderitext = gonderiPaylas.getText().toString();
                gonderiKaydet(gonderitext);
                gonderiPaylas.setText("");

            }
        });

        takipEdilenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),TakipEdilenleriGoster.class);
                intent.putExtra("kullaniciID",kullaniciID);
                startActivity(intent);
            }
        });
        takipciText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),TakipcileriGoster.class);
                intent.putExtra("kullaniciID",kullaniciID);
                startActivity(intent);
            }
        });


    }
    public void tanimla()
    {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        timelineReference = firebaseDatabase.getReference();
        takipEdilenreference = firebaseDatabase.getReference();
        takipciReference = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();
        gonderiPaylas = findViewById(R.id.gonderiPaylas);
        gonderiPaylasButon = findViewById(R.id.gonderiPaylasButon);
        takipEdilenText =findViewById(R.id.takipEdilenText);
        takipciText = findViewById(R.id.takipciText);
        id = findViewById(R.id.id);
        isim = findViewById(R.id.isim);
        bio = findViewById(R.id.bio);
        gonderiSayiInt = findViewById(R.id.gonderiSayiInt);
        takipEdilenSayiInt = findViewById(R.id.takipEdilenSayiInt);
        takipciSayiInt = findViewById(R.id.takipciSayiInt);
        list = new ArrayList<>();
        takipciListe = new ArrayList<>();
        takipEdilenListe = new ArrayList<>();
        timelineReyc = findViewById(R.id.timelineReyc);



        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HomePageActivity.this,1);
        timelineReyc.setLayoutManager(layoutManager);


        timelineAdapter = new TimelineAdapter(HomePageActivity.this,list, HomePageActivity.this,kullaniciID);
        timelineReyc.setAdapter(timelineAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Lütfen Bekleyiniz");
        progressDialog.setMessage("Veriler getiriliyor");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void veriCek()
    {
        String userID = firebaseAuth.getUid();
        databaseReference.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                kullaniciID = snapshot.child("kullaniciID").getValue().toString();
                String profilisim = snapshot.child("adin").getValue().toString();
                String profilbio = snapshot.child("bio").getValue().toString();

                isim.setText(profilisim);
                id.setText(kullaniciID);
                bio.setText(profilbio);
                progressDialog.dismiss();
                takipEdilen();
                takipciSayisi();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void gonderiKaydet(String gonderi)
    {
        gonderiKey = timelineReference.child("TimeLine").push().getKey();

        Map timeLineMap = new HashMap<>();
        timeLineMap.put("text",gonderi);
        timeLineMap.put("from",kullaniciID);
        timeLineMap.put("tarih",tarih());


        timelineReference.child("TimeLine").child(gonderiKey).setValue(timeLineMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {

                    Toast.makeText(getApplicationContext(),"Gönderiniz başarıyla paylaşıldı.",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    public String tarih()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm"+"    "+"dd:MM:yyyy");
        Date date = new Date();
        String tarih = dateFormat.format(date);

        return tarih;
    }
    public void loadTimeline()
    {
        reference.child("TimeLine").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {


               TimelineModel timeline = snapshot.getValue(TimelineModel.class);

                if(timeline.getFrom().equals(kullaniciID)){
                    list.add(timeline);

                    // bu if sayesinde sadece kendi paylastıklarımız gozukuyor.
                    timelineAdapter.notifyDataSetChanged();
                    timelineReyc.scrollToPosition(list.size() - 1);

                    //int sayi = list.size();
                   // String sayi1 = String.valueOf(sayi);
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
    public void takipEdilen()
    {
        takipEdilenreference.child("Takip").child("takipListesi").child(kullaniciID).child("takipEdilen").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String takipEdilenler = snapshot.getValue().toString();
                    takipEdilenListe.add(takipEdilenler);
                    takipEdilenSayiInt.setText(String.valueOf(takipEdilenListe.size()));

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
    public void takipciSayisi()
    {
        takipEdilenreference.child("Takip").child("takipListesi").child(kullaniciID).child("takipci").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String takipciler = snapshot.getValue().toString();
                takipciListe.add(takipciler);
                takipciSayiInt.setText(String.valueOf(takipciListe.size()));

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