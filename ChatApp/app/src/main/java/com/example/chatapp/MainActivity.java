package com.example.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText emailTextKayit, sifreTextKayit, kullaniciID,kullaniciBiografi,kullaniciIsmi;
    Button kayitOl;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kayitOlDonustur();
            }
        });


    }

    public void tanimla() {
        mAuth= FirebaseAuth.getInstance();

        emailTextKayit = findViewById(R.id.emailTextKayit);
        sifreTextKayit = findViewById(R.id.sifreTextKayit);
        kullaniciID = findViewById(R.id.kullaniciID);
        kullaniciBiografi = findViewById(R.id.kullaniciBiografi);
        kullaniciIsmi = findViewById(R.id.kullaniciIsmi);
        kayitOl = findViewById(R.id.kayitOl);
        progressDialog = new ProgressDialog(this);


    }

    public void kayitOlDonustur() {
        String eposta = emailTextKayit.getText().toString();
        String sifre = sifreTextKayit.getText().toString();
        String kullaniciId = kullaniciID.getText().toString();
        String biyografi =kullaniciBiografi.getText().toString();
        String isim =kullaniciIsmi.getText().toString();

        progressDialog.setTitle("Lütfen bekleyiniz");
        progressDialog.setMessage("Kayit olma işlemi gerçekleşiyor");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (!TextUtils.isEmpty(eposta) || !TextUtils.isEmpty(sifre))
        {

            registerUser(eposta, sifre,kullaniciId,biyografi,isim);
        }


    }

    public void registerUser (String eposta,String sifre,String kullaniciId,String biyografi,String isim){

        mAuth.createUserWithEmailAndPassword(eposta,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    String user_id =mAuth.getCurrentUser().getUid();  // uniq user id
                    mDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("kullaniciID",kullaniciId);
                    userMap.put("adin",isim);
                    userMap.put("bio",biyografi);
                    
                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this,GirisYap.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Kayıt olma işlemi başarılı",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Kayıt olma işlemi gerçekleşmedi",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }




}
