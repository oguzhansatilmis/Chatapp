package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView chatUserName;
    String kendiID,digerkullaniciID,digerkullaniciAdi,kendikullaniciAdi;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference kendiKullaniciAdim,onunKullaniciAdi,MesajKaydet,reference;
    FirebaseAuth mAuth;
    RecyclerView chatRecycviwe;
    EditText mesajEdt;
    ImageView ikinciİmage;
    MesajAdapter mesajAdapter;
    List<MesajModel> list ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth= FirebaseAuth.getInstance();
        digerkullaniciID =  getIntent().getExtras().getString("digerKullaniciID"); // profil ekranınanda gelen diger kullanici ıd;
        kendiID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        kendiKullaniciAdim = firebaseDatabase.getReference();
        onunKullaniciAdi = firebaseDatabase.getReference();
        tanimla();

    }
    public  void tanimla()
    {

        list = new ArrayList<>();
        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        kendiKullaniciAdim = firebaseDatabase.getReference();
        onunKullaniciAdi = firebaseDatabase.getReference();
        MesajKaydet = firebaseDatabase.getReference();
        reference = firebaseDatabase.getReference();

        chatUserName = findViewById(R.id.chatUserName);
        chatRecycviwe = findViewById(R.id.chatRecycviwe);
        mesajEdt = findViewById(R.id.mesajEdt);
        ikinciİmage = findViewById(R.id.ikinciİmage);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this,1);
        chatRecycviwe.setLayoutManager(layoutManager);

        kendiKullaniciAdim.child("Users").child(kendiID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kendikullaniciAdi = snapshot.child("kullaniciID").getValue().toString();
                mesajAdapter = new MesajAdapter(ChatActivity.this,list,ChatActivity.this,kendikullaniciAdi);
                chatRecycviwe.setAdapter(mesajAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        onunKullaniciAdi.child("Users").child(digerkullaniciID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                digerkullaniciAdi = snapshot.child("kullaniciID").getValue().toString();
                chatUserName.setText(digerkullaniciAdi);
                loadMesaj();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        ikinciİmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = mesajEdt.getText().toString();
                mesajEdt.setText("");
                 mesajGonder(mesaj);


            }
        });


    }
    public void mesajGonder(String text){
        String key = MesajKaydet.child("Mesajlar").child(kendikullaniciAdi).child(digerkullaniciAdi).push().getKey(); //mesajların dbye eklenmesi uniq idler olusturuldu

        Map mesajMap = new HashMap<>();
        mesajMap.put("text",text);
        mesajMap.put("from",kendikullaniciAdi);
        mesajMap.put("tarih",tarih());

        MesajKaydet.child("Mesajlar").child(kendikullaniciAdi).child(digerkullaniciAdi).child(key).setValue(mesajMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    MesajKaydet.child("Mesajlar").child(digerkullaniciAdi).child(kendikullaniciAdi).child(key).setValue(mesajMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //mesajları dbye katdetme
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Mesaj Gönderildi",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
    public void loadMesaj()
    {
        //mesajı getirme
        reference.child("Mesajlar").child(kendikullaniciAdi).child(digerkullaniciAdi).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MesajModel mesajModel = snapshot.getValue(MesajModel.class);  // guncelleme varsa ekler
                list.add(mesajModel);
                mesajAdapter.notifyDataSetChanged(); // real time (hemen ekler))

                chatRecycviwe.scrollToPosition(list.size()-1); // yeni mesajın altta gorunmesı

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

    public String tarih()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String tarih = dateFormat.format(date);

        return tarih;
    }






}






