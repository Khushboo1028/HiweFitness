package com.rgala98.hiwefitness.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

import java.util.HashMap;
import java.util.Map;

public class HealthGoalActivity extends AppCompatActivity {

    public static final String TAG = "HealthGoalActivity";
    RadioGroup radioGroup;
    Button btn_continue;

    String name,gender,number,email,health_goal;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), HealthGoalActivity.this);
        setContentView(R.layout.activity_health_goal);

        init();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(HealthGoalActivity.this,R.color.white));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.i(TAG,"i is "+i);
                switch(i){
                    case R.id.health_goal_1:
                        health_goal = getString(R.string.health_goal_1);
                        break;
                    case R.id.health_goal_2:
                        health_goal = getString(R.string.health_goal_2);
                        break;
                    case R.id.health_goal_3:
                        health_goal = getString(R.string.health_goal_3);
                        break;
                    case R.id.health_goal_4:
                        health_goal = getString(R.string.health_goal_4);
                        break;
                    case R.id.health_goal_5:
                        health_goal = getString(R.string.health_goal_5);
                        break;
                }
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });

    }

    private void init(){

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        gender = getIntent().getStringExtra("gender");
        number = firebaseUser.getPhoneNumber();
    }

    private void addData(){

        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> data = new HashMap<>();

        data.put(getString(R.string.contact_number), number);
        data.put(getString(R.string.name),name);
        data.put(getString(R.string.email),email);
        data.put(getString(R.string.gender),gender);
        data.put(getString(R.string.health_goal),health_goal);

        DocumentReference docRef = db.collection(getString(R.string.users)).document(firebaseUser.getUid());
        docRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);

                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), ReadyToGoActivity.class);
                startActivity(intent);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                showMessage("An Error Occurred","Connection Timeout");
                Log.i(TAG,"An Error Occurred" +e.getMessage());
            }
        });

    }


    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(HealthGoalActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
