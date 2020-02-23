package com.rgala98.hiwefitness.Profile;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rgala98.hiwefitness.Login.GettingStartedActivity;
import com.rgala98.hiwefitness.R;
import com.rgala98.hiwefitness.Utility.DefaultTextConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public static final int RESULT_LOAD_IMAGE = 18;
    public static final int RESULT_CAMERA = 23;
    View view;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    TextView logout,tv_name,tv_number,tv_email,tv_goal;
    ListenerRegistration listenerRegistration;
    CircleImageView profile_image;

    String imageURL = "",old_image_url;
    Uri file_camera_uri;

    ProgressBar progressBar;
    RelativeLayout superLayout;
    Uri downloadUri;
    StorageTask uploadTask;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), getActivity());
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(view.getContext().getColor(R.color.colorPrimary));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }



        init();

        //for camera intent
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

                Intent intent = new Intent(getContext(), GettingStartedActivity.class);
                startActivity(intent);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShowPhoto();
            }
        });

        getData();
        return view;
    }


    private void init(){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        logout = view.findViewById(R.id.logout);

        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_goal = (TextView) view.findViewById(R.id.tv_health_goal);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        superLayout = (RelativeLayout) view.findViewById(R.id.superLayout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void getData() {
        DocumentReference doc_ref= FirebaseFirestore.getInstance().collection(getString(R.string.users)).document(firebaseUser.getUid());

        listenerRegistration = doc_ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                if(e!=null){
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if(snapshot.exists()){
                    Log.i(TAG,"Data found with "+snapshot.getData());

                    tv_name.setText(snapshot.getString(getString(R.string.name)));
                    tv_email.setText(snapshot.getString(getString(R.string.email)));
                    tv_number.setText(firebaseUser.getPhoneNumber());
                    tv_goal.setText(snapshot.getString(getString(R.string.health_goal)));

                    if(snapshot.get("profile_url")!=null){
                        imageURL = snapshot.get("profile_url").toString();
                        Log.i(TAG,"imageURL is "+imageURL);
                        Glide.with(getContext()).load(imageURL).placeholder(R.drawable.ic_profile).into(profile_image);
                        old_image_url=imageURL;

                    }

                }else{
                    Log.i(TAG,"No such document");

                }

            }
        });
    }


    public void dialogShowPhoto() {
        final String takePhoto = "Take Photo";
        final String chooseFromLibrary = "Choose from Gallery";


        final CharSequence[] items = {takePhoto, chooseFromLibrary};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        //builder.setTitle(addPhoto);


        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals(chooseFromLibrary)) {


                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {



                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) == true){

                            showMessage("Uh-Oh", "Seems like you denied permission to access library! Go on app settings to grant permission");



                        }else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RESULT_LOAD_IMAGE);
                        }



                    }else{


                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, RESULT_LOAD_IMAGE);

                    }
                }


                else if (items[item].equals(takePhoto)) {

                    if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CAMERA) == true){


                            showMessage("Uh-Oh", "Seems like you denied permission to access camera! Go on app settings to grant permission");


                        }else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                                    RESULT_CAMERA);


                        }


                    }else{

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file_camera_uri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,file_camera_uri );
                        startActivityForResult(intent, RESULT_CAMERA);
                    }

                }

            }
        });
        builder.show();


    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Grace");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        RESULT_LOAD_IMAGE);

                showMessage("Uh-Oh", "Please grant permission to access library");

            } else {

                if(data.getData()!=null) {

                    Uri mImageUri = data.getData();
                    profile_image.setImageURI(mImageUri);
                    Log.i(TAG,"I am here 2");
                    uploadImage(mImageUri);

                }
            }


        }

        if (requestCode == RESULT_CAMERA && resultCode == Activity.RESULT_OK) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        RESULT_CAMERA);

            }else {

                Log.i(TAG,"Here");
                if(file_camera_uri!=null) {
                    Log.i(TAG, "Here 2");
                    profile_image.setImageURI(file_camera_uri);
                    uploadImage(file_camera_uri);
                }

            }



        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (grantResults!=null && grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults!=null && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    }
                }
                break;

            case RESULT_CAMERA:

                if (grantResults!=null && grantResults.length > 0 && permissions[0].equals(Manifest.permission.CAMERA)) {
                    // check whether storage permission granted or not.
                    if (grantResults!=null && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        file_camera_uri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,file_camera_uri );
                        startActivityForResult(intent, RESULT_CAMERA);
                    }
                }
                break;

            default:
                break;


        }
    }

    private void uploadImage(Uri selectedImageURI) {
        progressBar.setVisibility(View.VISIBLE);
        //delete old image
        Log.i(TAG, "Old_image url is" + old_image_url);
        if (old_image_url != null) {

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(old_image_url);
            mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.i(TAG, "Previous profile_image deleted");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "An error occurred " + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        //upload new image
        final StorageReference ref = storageReference.child("profile_images/"+ UUID.randomUUID().toString());


        try {

            Bitmap bitmap_photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageURI);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap_photo.compress(Bitmap.CompressFormat.JPEG,50,stream);
            byte[] byteArray = stream.toByteArray();
            uploadTask=ref.putBytes(byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    downloadUri = task.getResult();
                    imageURL= downloadUri.toString();

                    if(firebaseUser!=null) {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection(getString(R.string.users)).document(firebaseUser.getUid());

                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e);
                                    return;
                                }

                            }
                        });


                        docRef.update(getString(R.string.profile_url), imageURL)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "Profile Url updated");
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "An error occurred " + e.getMessage());
                                progressBar.setVisibility(View.GONE);
                                Snackbar snackbar = Snackbar.make(superLayout, "An error occurred", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();

                            }
                        });



                    }

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
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listenerRegistration!= null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

}
