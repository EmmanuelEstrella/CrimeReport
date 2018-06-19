package com.example.emman.crimereport.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.emman.crimereport.Models.Photo;
import com.example.emman.crimereport.Adapters.PhotoAdapter;
import com.example.emman.crimereport.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

    private Button mButton;
    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;
    private static final int GALLERY_INTENT = 1;
    private RecyclerView mRecyclerView;
    private PhotoAdapter adapter;
    private EditText mDescriptionText;
    private EditText mLocationText;
    private Photo photo;
    private ImageButton mlocationbtn;
    private ArrayList<Photo> photos;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mlocation;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myDataBaseRef = database.getReference("Photo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);


        mButton = findViewById(R.id.photo_btn);
        mDescriptionText = findViewById(R.id.descrption_text);
        mLocationText = findViewById(R.id.location_text);
        mlocationbtn = findViewById(R.id.location_btn);
        mProgressDialog = new ProgressDialog(this);
        photo = new Photo();


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        mLocationCallback = new LocationCallback() {
            public String TAG ="location";

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        mLocationText.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }

            ;
        };
        mlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
                //stopLocationUpdates();

            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mProgressDialog.setTitle("Uploading...");
            mProgressDialog.setMessage("Uploading Photo");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();


            Uri uri = data.getData();
            StorageReference filepath = mStorageRef.child("Photo").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    String TAG = "hola";
                    Uri download = taskSnapshot.getDownloadUrl();
                    Log.d(TAG, download.toString());
                    photo.setDescription(mDescriptionText.getText().toString());
                    photo.setLocation(mLocationText.getText().toString());
                    photo.setUrl(download.toString());
                    myDataBaseRef.push().setValue(photo);

                    startActivity(new Intent(UploadActivity.this, MainActivity.class));

                    // Toast.makeText(MainActivity.this, "Se subio la foto",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permision", "permision denided");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(new LocationRequest(), mLocationCallback,
                null /* Looper */);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


}