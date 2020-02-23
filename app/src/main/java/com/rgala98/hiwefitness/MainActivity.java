package com.rgala98.hiwefitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bigscreen.iconictabbar.view.IconicTab;
import com.bigscreen.iconictabbar.view.IconicTabBar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rgala98.hiwefitness.Login.GettingStartedActivity;
import com.rgala98.hiwefitness.Nearby.NearbyFragment;
import com.rgala98.hiwefitness.Profile.ProfileFragment;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;
import com.rgala98.hiwefitness.Workout.WorkoutFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    FragmentTransaction fragmentTransaction;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    IconicTabBar iconicTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), MainActivity.this);
        setContentView(R.layout.activity_main);

        init();


        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentTransaction.isEmpty()){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_tab, new NearbyFragment());
            fragmentTransaction.commit();
        }


        checkForLogOut();
        iconicTabBar.setOnTabSelectedListener(new IconicTabBar.OnTabSelectedListener() {

            @Override
            public void onSelected(IconicTab tab, int position) {
                Log.d(TAG, "selected tab on= " + position);
                int tabId = tab.getId();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (tabId) {

                case R.id.nearby:
                    fragmentTransaction.replace(R.id.frame_tab, new NearbyFragment());
                    break;

                case R.id.home:
                    fragmentTransaction.replace(R.id.frame_tab, new WorkoutFragment());
                    break;

                case R.id.profile:
                    fragmentTransaction.replace(R.id.frame_tab, new ProfileFragment());
                    break;

                }
                fragmentTransaction.commit();
            }

            @Override
            public void onUnselected(IconicTab tab, int position) {
                Log.d(TAG, "unselected tab on= " + position);
            }
        });



    }

    private void checkForLogOut(){

        if(firebaseUser!=null){
            DocumentReference doc_ref= FirebaseFirestore.getInstance().collection(getString(R.string.users)).document(firebaseUser.getUid());

            doc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if(!snapshot.exists()){
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), GettingStartedActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }


    }



    private void init(){

        iconicTabBar = (IconicTabBar) findViewById(R.id.tab_bar);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(firebaseUser==null){
            finishAffinity();
            Intent intent = new Intent(getApplicationContext(), GettingStartedActivity.class);
            startActivity(intent);
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
