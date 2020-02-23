package com.rgala98.hiwefitness.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

public class GettingStartedActivity extends AppCompatActivity {

    Button btn_getting_started;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), GettingStartedActivity.this);
        setContentView(R.layout.activity_getting_started);

        btn_getting_started = (Button) findViewById(R.id.btn_getting_started);

        btn_getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), VerifyNumberActivity.class);
                startActivity(intent);

            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(GettingStartedActivity.this,R.color.white));// set status background white
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
