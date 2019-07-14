package com.example.android.tripmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Adapter.PlaceAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Places extends AppCompatActivity {
    EditText category,des,date;
    String cat,desc,dov,tripName;
    FirebaseDatabase db;
    DatabaseReference ref;
    List<RecommendedPlace> PlaceList=new ArrayList<>();
    RecyclerView rvPlaces;
    PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        db=FirebaseDatabase.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarPlaces);
        setSupportActionBar(toolbar);
        rvPlaces=findViewById(R.id.rvPlaces);
        ImageView img=findViewById(R.id.imgplaces);
        Glide.with(this).load("https://images.unsplash.com/photo-1528999101658-2ed042724a1f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        Intent intent=getIntent();
        tripName=intent.getStringExtra("TripName");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.placesmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.locate:
                AddLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddLocation() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Trip");
        builder.setIcon(R.drawable.ic_edit_location);
        LinearLayout l=new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        category=new EditText(this);
        category.setHint("Enter Category");
        l.addView(category);
        des=new EditText(this);
        des.setHint("Enter Description");
        l.addView(des);
        date=new EditText(this);
        date.setHint("Enter Date of Visit");
        l.addView(date);
        builder.setView(l);
        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cat=category.getText().toString();
                desc=des.getText().toString();
                dov=date.getText().toString();
                if(!TextUtils.isEmpty(cat)&&!TextUtils.isEmpty(desc)&&!TextUtils.isEmpty(dov)) {
                    RecommendedPlace place = new RecommendedPlace(tripName, cat, desc, dov, Common.currentUser.getUsername());
                    ref.push().setValue(place);
                    Toast.makeText(Places.this, "Recommendation Added Successfully ", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Places.this, "Cannot create empty Recommendation", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PlaceList.clear();
        ref=db.getReference("RecommendedPlaces");
        ref.orderByChild("tripName").equalTo(tripName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RecommendedPlace place=dataSnapshot.getValue(RecommendedPlace.class);
                PlaceList.add(place);
                displayPlaces(PlaceList);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                List<RecommendedPlace>newList=new ArrayList<>();
                RecommendedPlace place=dataSnapshot.getValue(RecommendedPlace.class);
                newList.add(place);
                PlaceList=newList;
                displayPlaces(PlaceList);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                List<RecommendedPlace>newList=new ArrayList<>();
                RecommendedPlace place=dataSnapshot.getValue(RecommendedPlace.class);
                newList.add(place);
                PlaceList=newList;
                displayPlaces(PlaceList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void displayPlaces(List<RecommendedPlace> placeList) {
        rvPlaces.setLayoutManager(new LinearLayoutManager(Places.this));
        placeAdapter=new PlaceAdapter(Places.this,placeList,ref);
        rvPlaces.setAdapter(placeAdapter);
        }
}
