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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

public class PersonalDetailsActivity extends AppCompatActivity {

    public static final String TAG = "PersonalDetailsActivity";
    Button btn_continue;
    EditText et_name;
    EditText et_email;
    ImageView img_male,img_female;
    String gender,number;


    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), PersonalDetailsActivity.this);
        setContentView(R.layout.activity_personal_details);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(PersonalDetailsActivity.this,R.color.white));

        init();


        img_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_female.setImageAlpha(80);
                img_male.setImageAlpha(255);
                gender="male";


            }
        });

        img_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_male.setImageAlpha(80);
                img_female.setImageAlpha(255);
                gender="female";
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_email.getText().toString().trim().isEmpty() || et_name.getText().toString().trim().isEmpty()){
                    showMessage("Oops!","Please enter all details!");
                }else{
                    Intent intent = new Intent (getApplicationContext(), HealthGoalActivity.class);
                    intent.putExtra("name",et_name.getText().toString());
                    intent.putExtra("gender",gender);
                    intent.putExtra("email",et_email.getText().toString().trim());
                    intent.putExtra("number",number);
                    startActivity(intent);

                }

            }
        });
    }

    private void init(){
        et_email = (EditText) findViewById(R.id.et_email);
        et_name = (EditText) findViewById(R.id.et_name);

        btn_continue = (Button) findViewById(R.id.btn_continue);
        img_female = (ImageView) findViewById(R.id.img_female);
        img_male = (ImageView) findViewById(R.id.img_male);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        img_female.setImageAlpha(80);
        gender="male";
        number = getIntent().getStringExtra("number");





    }



    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(PersonalDetailsActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
