package com.example.android.tripmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Adapter.TripAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Final extends AppCompatActivity  {
    FirebaseDatabase db;
    DatabaseReference tripDb;
    TripAdapter tripAdapter;
    List<Trip> trips=new ArrayList<>();
    RecyclerView rvTrip;
    EditText myTrip;
    public String TripInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=FirebaseDatabase.getInstance();
        rvTrip=findViewById(R.id.rvTrip);
        ImageView img=findViewById(R.id.img);
        Glide.with(this).load("https://images.pexels.com/photos/544268/pexels-photo-544268.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawermenu, menu);
        MenuItem searchItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tripAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add:
                addTrip();
                return true;
            case R.id.about:
                startActivity(new Intent(Final.this,AboutActivity.class));
                return true;
            case R.id.logout:
                onBackPressed();
                return true;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!");
                startActivity(shareIntent);
            case R.id.faq:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://tripsource.com/faq/")));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTrip() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Trip");
        builder.setIcon(R.drawable.ic_flight);
        myTrip=new EditText(this);
        myTrip.setHint("My awesome trip");
        builder.setView(myTrip);
        builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TripInput=myTrip.getText().toString();
                if (!TextUtils.isEmpty(TripInput)){
                    Trip trip = new Trip(TripInput, Common.currentUser.getUsername());
                    tripDb.push().setValue(trip);
                    Toast.makeText(Final.this, "Trip Created Successfully ", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Cannot create empty trip", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        trips.clear();
        tripDb=db.getReference("trips");
        tripDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Trip trip=dataSnapshot.getValue(Trip.class);
                trip.setTripKey(dataSnapshot.getKey());
                trips.add(trip);
                displayTrips(trips);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Trip trip=dataSnapshot.getValue(Trip.class);
                trip.setTripKey(dataSnapshot.getKey());
                List<Trip>newTrips=new ArrayList<>();
                for(Trip t:trips)
                {
                    if(t.getTripKey().equals(trip.getTripKey()))
                        newTrips.add(trip);
                    else
                        newTrips.add(t);
                }
                trips=newTrips;
                displayTrips(trips);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Trip trip=dataSnapshot.getValue(Trip.class);
                trip.setTripKey(dataSnapshot.getKey());
                List<Trip>newTrips=new ArrayList<>();
                for(Trip t:trips)
                {
                    if(!t.getTripKey().equals(trip.getTripKey()))
                        newTrips.add(t);
                }
                trips=newTrips;
                displayTrips(trips);


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayTrips(List<Trip> trips) {
        rvTrip.setLayoutManager(new LinearLayoutManager(Final.this));
        tripAdapter=new TripAdapter(Final.this,trips,tripDb);
        rvTrip.setAdapter(tripAdapter);
    }


}
