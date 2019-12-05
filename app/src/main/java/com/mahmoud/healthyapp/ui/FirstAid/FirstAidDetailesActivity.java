package com.mahmoud.healthyapp.ui.FirstAid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mahmoud.healthyapp.R;
import com.mahmoud.healthyapp.pojo.FirstAid;

import java.util.Locale;

import io.paperdb.Paper;

public class FirstAidDetailesActivity extends AppCompatActivity {

    TextView txt_name;

    TextView txt_body;

    Intent intent;
    String firstAidName, lang;
    FirebaseFirestore firestore;
    DocumentReference ducRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsr_aid_detailes);


        Paper.init(this);
        lang = Paper.book().read("language","en");
        setLocale(lang);
        intent = getIntent();
        firstAidName = intent.getStringExtra("name");

        txt_name=findViewById(R.id.txt_name);
        txt_body=findViewById(R.id.txt_body);

        firestore = FirebaseFirestore.getInstance();

        ducRef = firestore.collection("FirstAids").document(firstAidName);
        ducRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    FirstAid firstAid=task.getResult().toObject(FirstAid.class);
                    if (lang.equals("ar")) {
                        txt_name.setText(firstAid.getAr_name());
                        txt_body.setText(firstAid.getAr_body());
                    }else {
                        txt_name.setText(firstAid.getEn_name());
                        txt_body.setText(firstAid.getEn_body());
                    }
                }
            }
        });
    }
    private void setLocale(String lang){
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config=new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","en");
        setLocale(language);
    }
}
