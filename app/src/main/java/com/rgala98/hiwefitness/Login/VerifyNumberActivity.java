package com.rgala98.hiwefitness.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

public class VerifyNumberActivity extends AppCompatActivity {

    Button btn_send;
    EditText et_number;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), VerifyNumberActivity.this);
        setContentView(R.layout.activity_verify_number);

        //change status bar color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(VerifyNumberActivity.this,R.color.white));

        init();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_number.getText().toString().trim().length()!=10){
                    showMessage("Error!","Please enter a 10 digit phone number.");
                }else if(et_number.getText().toString().charAt(0) != '9' &&
                        et_number.getText().toString().charAt(0) != '8'  &&
                        et_number.getText().toString().charAt(0) != '7'  &&
                        et_number.getText().toString().charAt(0) != '6'){
                    showMessage("Error","Please enter a valid phone number");
                }else {
                    number = "+91"+et_number.getText().toString().trim();
                    showMessageOptions("Send OTP to " + number, getString(R.string.app_name)+" will send you an OTP to verify your mobile number. You will receive SMS for verification and standard rates may apply.");

                }


            }
        });
    }

    private void init(){
        btn_send = (Button) findViewById(R.id.btn_send);
        et_number = (EditText) findViewById(R.id.et_number);

    }


    public void showMessageOptions(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(VerifyNumberActivity.this);
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intent = new Intent (getApplicationContext(), EnterOtpActivity.class);
                intent.putExtra("number",number);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(VerifyNumberActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
