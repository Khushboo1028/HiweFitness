package com.rgala98.hiwefitness.Nearby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewGymActivity extends AppCompatActivity {

    ImageView back;
    ImageView gym_photo;
    TextView tv_about_gym, tv_gym_address, tv_time, tv_gym_name;
    Button btn_connect,btn_navigate;
    int pos;

    ArrayList<ContentsNearby> nearbyArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), ViewGymActivity.this);

        setContentView(R.layout.activity_view_gym);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.colorPrimary));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(!nearbyArrayList.isEmpty()){
          tv_gym_address.setText(nearbyArrayList.get(pos).getFull_address());
          tv_about_gym.setText(nearbyArrayList.get(pos).getAbout_gym());
          tv_gym_name.setText(nearbyArrayList.get(pos).getGym_name());
          tv_time.setText(String.valueOf(nearbyArrayList.get(pos).getStart_time()) + " - " + nearbyArrayList.get(pos).getEnd_time());

            Glide
                    .with(getApplicationContext())
                    .load(nearbyArrayList.get(pos).getImage_url())
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile_background)
                    .into(gym_photo);

        }



        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message="Hello, I would like to connect to "+ nearbyArrayList.get(pos).getGym_name() + "\n\n - Sent from Hiwe Fitness for Android";
                message = message.replace(" ","%20");
                message = message.replace("\n","%0a");

                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone="+"+917901792967"+"&text="+message);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        btn_navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map = "http://maps.google.co.in/maps?q="+nearbyArrayList.get(pos).getGym_name()+" "+nearbyArrayList.get(pos).getFull_address();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });
    }



    private void init(){
        back = (ImageView) findViewById(R.id.back);
        gym_photo = (ImageView) findViewById(R.id.gym_photo);
        tv_about_gym = (TextView) findViewById(R.id.tv_about_gym);
        tv_gym_name = (TextView) findViewById(R.id.tv_gym_name);
        tv_gym_address = (TextView) findViewById(R.id.tv_gym_address);
        gym_photo = (ImageView) findViewById(R.id.gym_photo);
        tv_time = (TextView) findViewById(R.id.tv_time);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_navigate = (Button) findViewById(R.id.btn_navigate);

        nearbyArrayList = new ArrayList<>();
        nearbyArrayList = getIntent().getParcelableArrayListExtra("nearbyArrayList");
        pos = getIntent().getIntExtra("position",0);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
