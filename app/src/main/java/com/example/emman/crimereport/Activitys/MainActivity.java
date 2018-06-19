package com.example.emman.crimereport.Activitys;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.emman.crimereport.Models.Photo;
import com.example.emman.crimereport.Adapters.PhotoAdapter;
import com.example.emman.crimereport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private RecyclerView mRecyclerView;
    private PhotoAdapter adapter;
    private Photo photo;
    private ArrayList<Photo> photos;
    //private FusedLocationProviderClient mFusedLocationClient;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myDataBaseRef = database.getReference("Photo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,UploadActivity.class));
            }
        });

        mRecyclerView = findViewById(R.id.recycler_view);
        photo = new Photo();
        adapter = new PhotoAdapter(MainActivity.this);
        photos = new ArrayList<Photo>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        myDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photos.removeAll(photos);
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                        ) {
                    Photo photo = snapshot.getValue(Photo.class);
                    photos.add(photo);
                }
                adapter.updateData(photos);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }




}
