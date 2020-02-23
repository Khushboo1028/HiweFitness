package com.rgala98.hiwefitness.Nearby;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rgala98.hiwefitness.Login.GettingStartedActivity;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;
import com.rgala98.hiwefitness.Utility.DisctanceCalculator;
import com.rgala98.hiwefitness.Utility.LocationTrack;
import com.rgala98.hiwefitness.Utility.SortingClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class NearbyFragment extends Fragment {

    public static final String TAG = "NearbyFragment";
    public static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 69;
    public static final int REQUEST_CHECK_SETTINGS = 23;

    View view;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private ArrayList<ContentsNearby> nearbyArrayList;
    private NearbyAdapter mAdapter;

    LocationTrack locationTrack;
    double longitude,latitude;
    TextView tv_location;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());

        view = inflater.inflate(R.layout.fragment_nearby, container, false);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(view.getContext().getColor(R.color.colorPrimary));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }




        init();



//        if(nearbyArrayList.isEmpty()){
//
//            showMessage("Uh-Oh","Kindly Check your internet connection or Make sure your GPS is turned on for best experience. ");
//            nearbyArrayList.add(new ContentsNearby(
//                    "Hiwe - Rudhra Gym and Fitness Centre",
//                    "https://lh5.googleusercontent.com/p/AF1QipNAvFr37WwCCsEPlZPptgKo531WwjO0r6nJxZSt=w408-h306-k-no",
//                    "Mukherjee Nagar, Delhi",
//                    "32/7, ground floor, gate no. 3, near Nirankari Colony, Indra Vikas Colony, Bhai Parmanand Colony, Mukherjee Nagar, Delhi, 110009",
//                    "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
//                    "05:00",
//                    "22:00",
//                    ""
//
//            ));
//
//
//            nearbyArrayList.add(new ContentsNearby(
//                    "Hiwe - Milestone Gym",
//                    "https://lh5.googleusercontent.com/p/AF1QipN79kHMqMQ3b-UMkMk26Aj-czM0XyRnfVTGmtvt=w408-h306-k-no",
//                    "Vijay Nagar, New Delhi",
//                    "C29A, Sant Kirpal Singh Marg, Vijay Nagar, New Delhi, Delhi 110009",
//                    "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
//                    "06:00",
//                    "23:00",
//                    ""
//
//
//            ));
//
//
//            nearbyArrayList.add(new ContentsNearby(
//                    "Hiwe - Fit In Gym (The next generation of Fitness)",
//                    "https://lh5.googleusercontent.com/p/AF1QipN3h5caRuq8hw9-RHCTH72XcL6hc8ZiTYyNONhw=w408-h544-k-no",
//                    "Kalyan Vihar, Delhi",
//                    "208-A, First Floor, Old Gupta Colony, Kalyan Vihar, Delhi, 110009",
//                    "With dedicated trainers and good quality equipment, this is a place that will help you meet your fitness goals without causing a dent in the pockets. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
//                    "06:00",
//                    "22:30",
//                    ""
//
//            ));
//
//            nearbyArrayList.add(new ContentsNearby(
//                    "Hiwe - Run And Pump Gym",
//                    "https://lh5.googleusercontent.com/p/AF1QipOmaDo7NLvzh3fOsjLEQ646xnf-ZT4afrIXbTVu=w408-h306-k-no",
//                    "Mukherjee Nagar, Delhi",
//                    "A-19,basement, priyanka tower, commercial complex, Mukherjee Nagar, Delhi, 110009",
//                    "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
//                    "06:00 - 12:00",
//                    "17:00 - 22:00",
//                    ""
//
//            ));
//            mAdapter.notifyDataSetChanged();
//        }
        return view;
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        nearbyArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//by default manager is vertical
        mAdapter = new NearbyAdapter(getActivity(),nearbyArrayList);
        recyclerView.setAdapter(mAdapter);


        tv_location = (TextView) view.findViewById(R.id.tv_location);

    }

    private void getLocation() throws IOException {
        nearbyArrayList.clear();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            }
        }
        else{
            displayLocationSettingsRequest(getContext());
            locationTrack = new LocationTrack(getActivity());

            if (locationTrack.canGetLocation()) {

                longitude = locationTrack.getLongitude();
                latitude = locationTrack.getLatitude();

                Log.i(TAG,"Latitude" + latitude);
                Log.i(TAG,"Longitude" +longitude);


                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getContext(), Locale.getDefault());


                try {
                    String address,city,state,country,postalCode,knownName,zipCode;
                    addresses =geocoder.getFromLocation(latitude,longitude, 5);

                    if (addresses.size() > 0) {

                         address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                         city = addresses.get(0).getLocality();
                         state = addresses.get(0).getAdminArea();
                         country = addresses.get(0).getCountryName();
                         postalCode = addresses.get(0).getPostalCode();
                         knownName = addresses.get(0).getFeatureName();

                         Log.i(TAG,"Known name is "+knownName);

                        //if 1st provider does not have data, loop through other providers to find it.
//                        int count = 0;
//                        while ( count < addresses.size()) {
//                            zipCode = addresses.get(count).getPostalCode();
//                            city= addresses.get(count).getLocality();
//                            country = addresses.get(count).getCountryName();
//                            state = addresses.get(count).getAdminArea();
//                            address = addresses.get(count).getAddressLine(0);
//                            knownName = addresses.get(count).getFeatureName();

//                            count++;
//                        }
//
//                        Log.i(TAG,"Known name is "+knownName);
                        tv_location.setText(knownName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }



                    DisctanceCalculator disctanceCalculator = new DisctanceCalculator();
                    double distanceFromCurrentLocation = disctanceCalculator.distance(latitude, longitude, 28.713718, 77.206547, "K");
                    Log.i(TAG, "Distance from current location is " + distanceFromCurrentLocation);

                    nearbyArrayList.add(new ContentsNearby(
                            "Hiwe - Rudhra Gym and Fitness Centre",
                            "https://lh5.googleusercontent.com/p/AF1QipNAvFr37WwCCsEPlZPptgKo531WwjO0r6nJxZSt=w408-h306-k-no",
                            "Mukherjee Nagar, Delhi",
                            "32/7, ground floor, gate no. 3, near Nirankari Colony, Indra Vikas Colony, Bhai Parmanand Colony, Mukherjee Nagar, Delhi, 110009",
                            "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
                            "05:00",
                            "22:00",
                            String.format("%.0f", distanceFromCurrentLocation)

                    ));

                    distanceFromCurrentLocation = disctanceCalculator.distance(latitude, longitude, 28.691617, 77.201419, "K");

                    nearbyArrayList.add(new ContentsNearby(
                            "Hiwe - Milestone Gym",
                            "https://lh5.googleusercontent.com/p/AF1QipN79kHMqMQ3b-UMkMk26Aj-czM0XyRnfVTGmtvt=w408-h306-k-no",
                            "Vijay Nagar, New Delhi",
                            "C29A, Sant Kirpal Singh Marg, Vijay Nagar, New Delhi, Delhi 110009",
                            "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
                            "06:00",
                            "23:00",
                            String.format("%.0f", distanceFromCurrentLocation)


                    ));

                    distanceFromCurrentLocation = disctanceCalculator.distance(latitude, longitude, 28.692059, 77.200913, "K");

                    nearbyArrayList.add(new ContentsNearby(
                            "Hiwe - Fit In Gym (The next generation of fitness)",
                            "https://lh5.googleusercontent.com/p/AF1QipN3h5caRuq8hw9-RHCTH72XcL6hc8ZiTYyNONhw=w408-h544-k-no",
                            "Kalyan Vihar, Delhi",
                            "208-A, First Floor, Old Gupta Colony, Kalyan Vihar, Delhi, 110009",
                            "With dedicated trainers and good quality equipment, this is a place that will help you meet your fitness goals without causing a dent in the pockets. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
                            "06:00",
                            "22:30",
                            String.format("%.0f", distanceFromCurrentLocation)

                    ));

                    distanceFromCurrentLocation = disctanceCalculator.distance(latitude, longitude, 28.711566, 77.216245, "K");

                    nearbyArrayList.add(new ContentsNearby(
                            "Hiwe - Run And Pump Gym",
                            "https://lh5.googleusercontent.com/p/AF1QipOmaDo7NLvzh3fOsjLEQ646xnf-ZT4afrIXbTVu=w408-h306-k-no",
                            "Mukherjee Nagar, Delhi",
                            "A-19,basement, priyanka tower, commercial complex, Mukherjee Nagar, Delhi, 110009",
                            "This is a great gym for your daily workout session where you can break a sweat and achieve your body goals. It is equipped with highly maintained equipment that will cater to all your needs. The trainers will sit and discuss with you what are your needs are and especially curate a regime for you with following up with it from time to time.",
                            "06:00 - 12:00",
                            "17:00 - 22:00",
                            String.format("%.0f", distanceFromCurrentLocation)

                    ));

                    SortingClass sortingClass = new SortingClass(nearbyArrayList);
                    Collections.reverse(sortingClass.sortDistanceLowToHigh());
                    mAdapter.notifyDataSetChanged();


            }else {
                Log.i(TAG,"Cannot find your location");

            }
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:

                if (grantResults!=null && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        getLocation();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }else{
                    showMessage("Uh-Oh","Seems like you have denied permission. The app cannot function normally");

                }

                break;




        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                            showMessage("Error","Can't find your location! Please ensure Mobile GPS is on!");

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        showMessage("Error","Can't find your location! Please ensure Mobile GPS is on!");

                        break;
                }
            }
        });
    }

    public void showMessage(String title, String message){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(firebaseUser==null){
            Intent intent = new Intent(getContext(), GettingStartedActivity.class);
            startActivity(intent);
        }else{
            try {
                getLocation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
