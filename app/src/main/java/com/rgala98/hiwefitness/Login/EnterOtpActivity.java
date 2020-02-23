package com.rgala98.hiwefitness.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rgala98.hiwefitness.MainActivity;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EnterOtpActivity extends AppCompatActivity {

    public static final String TAG = "EnterOtpActivity";
    Button btn_Verify;
    EditText et_number_1;
    EditText et_number_2;
    EditText et_number_3;
    EditText et_number_4;
    EditText et_number_5;
    EditText et_number_6;

    String otp;
    String number;
    TextView tv_resend_code;
    ProgressBar progressBar;

    //firebase
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private boolean mVerificationInProgress = false,verificationSuccessful;
    private PhoneAuthCredential credential_global;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), EnterOtpActivity.this);
        setContentView(R.layout.activity_enter_otp);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(EnterOtpActivity.this,R.color.white));

        init();

        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = et_number_1.getText().toString().trim() + et_number_2.getText().toString().trim()+et_number_3.getText().toString().trim()
                        +et_number_4.getText().toString().trim()+et_number_5.getText().toString().trim()+et_number_6.getText().toString().trim();

//                Log.i(TAG,"OTP Entered is "+otp);

                if(otp==null || otp.isEmpty() || otp.length()!=6){

                    btn_Verify.setEnabled(true);
                    showMessage("Uh-Oh","Please enter correct OTP");
                }else{

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    Log.i(TAG,"Credential is "+credential);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        if(number!=null){
            getOtp();
        }

        shiftEditTextFocus();

        tv_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(number,mResendToken);
            }
        });


    }

    private void init(){
        btn_Verify = (Button) findViewById(R.id.btn_verify);
        et_number_1 = (EditText) findViewById(R.id.et_number_1);
        et_number_2 = (EditText) findViewById(R.id.et_number_2);
        et_number_3 = (EditText) findViewById(R.id.et_number_3);
        et_number_4 = (EditText) findViewById(R.id.et_number_4);
        et_number_5 = (EditText) findViewById(R.id.et_number_5);
        et_number_6 = (EditText) findViewById(R.id.et_number_6);

        tv_resend_code = (TextView) findViewById(R.id.tv_resend_code);

        number = getIntent().getStringExtra("number");
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
    }

    private void shiftEditTextFocus(){
        et_number_1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

                if(et_number_1.getText().toString().length()==1)     //size as per your requirement
                {
                    et_number_2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {


            }

            public void afterTextChanged(Editable s) {

            }

        });

        et_number_2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et_number_2.getText().toString().length()==1)     //size as per your requirement
                {
                    et_number_3.requestFocus();
                }

                if(TextUtils.isEmpty(et_number_2.getText().toString())){
                    et_number_1.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });

        et_number_3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et_number_3.getText().toString().length()==1)     //size as per your requirement
                {
                    et_number_4.requestFocus();
                }

                if(TextUtils.isEmpty(et_number_3.getText().toString())){
                    et_number_2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });
        et_number_4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et_number_4.getText().toString().length()==1)     //size as per your requirement
                {
                    et_number_5.requestFocus();
                }

                if(TextUtils.isEmpty(et_number_4.getText().toString())){
                    et_number_3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });

        et_number_5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                if(et_number_5.getText().toString().length()==1)     //size as per your requirement
                {
                    et_number_6.requestFocus();
                }

                if(TextUtils.isEmpty(et_number_5.getText().toString())){
                    et_number_4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });

        et_number_6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {

                if(TextUtils.isEmpty(et_number_5.getText().toString())){
                    et_number_4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });

    }

    private void getOtp() {


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(final PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;

                //for phones that automatically detect otp and move ahead
                verificationSuccessful=true;

                credential_global=credential;
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                mVerificationInProgress = false;


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    showMessage("Oops!","Your phone number is invalid");
                    finish(); //go to previous activity to re enter phone number


                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(getApplicationContext(),"Too many requests have been sent on this number. Please try again later",Toast.LENGTH_LONG).show();
                    Log.i(TAG," here have been too many requests sent on this number");
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(),"OTP Sent",Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;
                btn_Verify.setVisibility(View.VISIBLE);
                btn_Verify.setEnabled(true);

            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);


    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            final DocumentReference doc_ref= FirebaseFirestore.getInstance().collection(getString(R.string.users)).document(user.getUid());

                            doc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        finishAffinity();
                                        Intent intent = new Intent(getApplicationContext(), PersonalDetailsActivity.class);
                                        intent.putExtra("number",number);
                                        startActivity(intent);


                                    }else{
                                        finishAffinity();
                                        Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(0,0);

                                    }
                                }
                            });



                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                progressBar.setVisibility(View.GONE);
                                showMessage("Oops!", "The verification code entered was invalid!");
                            }

                            progressBar.setVisibility(View.GONE);

                            if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                showMessage("Oops!", "Error: " +task.getException().getMessage());
                            }

                        }
                    }
                });
    }

    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(EnterOtpActivity.this);
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }



}
