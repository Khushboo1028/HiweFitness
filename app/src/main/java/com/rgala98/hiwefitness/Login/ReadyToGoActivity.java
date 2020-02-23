package com.rgala98.hiwefitness.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rgala98.hiwefitness.MainActivity;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

public class ReadyToGoActivity extends AppCompatActivity {

    Button btn_getting_started;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), ReadyToGoActivity.this);
        setContentView(R.layout.activity_ready_to_go);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(ReadyToGoActivity.this,R.color.colorPrimary));

        btn_getting_started = (Button) findViewById(R.id.btn_getting_started);

        btn_getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
