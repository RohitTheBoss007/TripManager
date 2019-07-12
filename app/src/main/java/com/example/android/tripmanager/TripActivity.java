package com.example.android.tripmanager;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripActivity extends AppCompatActivity {
    public static final int REQ_CODE = 0;
    List<String> members = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference ref, dr;
    RecyclerView rvMembers;
    MemberAdapter memberAdapter;
    String TripName, creator, MemberAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        Toolbar toolbar = findViewById(R.id.toolbarTrip);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        rvMembers = findViewById(R.id.rvMembers);
        Intent intent = getIntent();
        TripName = intent.getStringExtra("Trip");
        creator = intent.getStringExtra("Creator");
        Log.d("ggggg", creator);
        ImageView img=findViewById(R.id.img1);
        Glide.with(this).load("https://images.pexels.com/photos/2279226/pexels-photo-2279226.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tripmenu, menu);
        MenuItem searchItem=menu.findItem(R.id.searchmem);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                memberAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addmem:
                addMem();
                return true;
            case R.id.groupchat:
                startActivity(new Intent(TripActivity.this,GroupChatActivity.class));
                return true;
            case R.id.places:
                startActivity(new Intent(TripActivity.this,Places.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addMem() {
        Intent i = new Intent(this, FindFriends.class);
        startActivityForResult(i, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    MemberAdded = data.getStringExtra("Name");
                    if (!MemberAdded.equals(creator) && !members.contains(MemberAdded)) {
                        members.add(MemberAdded);
                        dr = database.getReference("trips");
                        dr.orderByChild("tripName").equalTo(TripName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String curkey = null;
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    curkey = childSnapshot.getKey();
                                }

                                Log.d("INFO12345", curkey);
                                dr.child(curkey).push().setValue(MemberAdded);
                                ref = database.getReference("trips").child(curkey);
                                ref.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        String name = dataSnapshot.getValue(String.class);
                                        if (!members.contains(name) && !name.equals(TripName)) {
                                            members.add(name);
                                            displayMembers(members);
                                        }

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        List<String> newMembers = new ArrayList<>();
                                        newMembers.add(creator);
                                        String name = dataSnapshot.getValue(String.class);
                                        if (!name.equals(TripName)) {
                                            newMembers.add(name);
                                            members = newMembers;
                                            displayMembers(members);
                                        }

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                                        List<String> newMembers = new ArrayList<>();
                                        newMembers.add(creator);
                                        String name = dataSnapshot.getValue(String.class);
                                        if (!name.equals(TripName)) {
                                            newMembers.add(name);
                                            members = newMembers;
                                            displayMembers(members);
                                        }

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

            }
        }


    }

    private void displayMembers(List<String> members) {
        rvMembers.setLayoutManager(new LinearLayoutManager(TripActivity.this));
        memberAdapter = new MemberAdapter(TripActivity.this, members, ref);
        rvMembers.setAdapter(memberAdapter);
        if (memberAdapter != null)
            memberAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Final.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        members.clear();
        dr = database.getReference("trips");
        dr.orderByChild("tripName").equalTo(TripName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String curkey = null;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    curkey = childSnapshot.getKey();
                }
                ref = database.getReference("trips").child(curkey);
                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name = dataSnapshot.getValue(String.class);
                        if (!members.contains(name) && !name.equals(TripName)) {
                            members.add(name);
                            displayMembers(members);
                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        List<String> newMembers = new ArrayList<>();
                        newMembers.add(creator);
                        String name = dataSnapshot.getValue(String.class);
                        if (!name.equals(TripName)) {
                            newMembers.add(name);
                            members = newMembers;
                            displayMembers(members);
                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        List<String> newMembers = new ArrayList<>();
                        newMembers.add(creator);
                        String name = dataSnapshot.getValue(String.class);
                        if (!name.equals(TripName)) {
                            newMembers.add(name);
                            members = newMembers;
                            displayMembers(members);
                        }

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}