package com.example.android.tripmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Adapter.ImageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PicturesActivity extends AppCompatActivity {
    String tripName;
    FirebaseDatabase db;
    DatabaseReference ref;
    RecyclerView rvPics;
    ImageAdapter imageAdapter;
    List<Upload> pics=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        Toolbar toolbar = findViewById(R.id.ptoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Pictures");
        Intent i=getIntent();
        rvPics=findViewById(R.id.rvPics);
        tripName=i.getStringExtra("TripName");
        db=FirebaseDatabase.getInstance();
        ImageView img=findViewById(R.id.imgback);
        Glide.with(this).load("https://images.unsplash.com/photo-1452570053594-1b985d6ea890?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPic:
                AddPic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddPic() {
        Intent intent=new Intent(PicturesActivity.this,AddPics.class);
        intent.putExtra("TripName",tripName);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pics.clear();
        ref=db.getReference("uploads");
        ref.orderByChild("tripName").equalTo(tripName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Upload u=dataSnapshot.getValue(Upload.class);
                pics.add(u);
                displayPics(pics);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                List<Upload>newPics=new ArrayList<>();
                Upload u=dataSnapshot.getValue(Upload.class);
                newPics.add(u);
                pics=newPics;

                displayPics(pics);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                List<Upload>newPics=new ArrayList<>();
                Upload u=dataSnapshot.getValue(Upload.class);
                newPics.add(u);
                pics=newPics;
                displayPics(pics);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayPics(List<Upload> pics) {
        rvPics.setLayoutManager(new LinearLayoutManager(PicturesActivity.this));
        imageAdapter=new ImageAdapter(PicturesActivity.this,pics,ref);
        rvPics.setAdapter(imageAdapter);
    }
}
