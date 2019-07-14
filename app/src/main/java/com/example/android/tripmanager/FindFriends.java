package com.example.android.tripmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Adapter.FriendAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FindFriends extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference users;
    FriendAdapter friendAdapter;
    List<User> friends=new ArrayList<>();
    RecyclerView rvFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Member");
        database=FirebaseDatabase.getInstance();
        rvFriends=findViewById(R.id.rvFind);
        ImageView img=findViewById(R.id.img2);
        Glide.with(this).load("https://images.pexels.com/photos/955798/pexels-photo-955798.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.finalmenu, menu);
        MenuItem searchItem=menu.findItem(R.id.searchFriend);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                friendAdapter.getFilter().filter(s);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        friends.clear();
        users=database.getReference("Users");
        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                User name = dataSnapshot.getValue(User.class);
                friends.add(name);


                displayFriends(friends);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                List<User>newNames=new ArrayList<>();

                User name = dataSnapshot.getValue(User.class);
                newNames.add(name);
                friends=newNames;
                displayFriends(friends);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                List<User>newNames=new ArrayList<>();

                User name = dataSnapshot.getValue(User.class);
                newNames.add(name);
                friends=newNames;
                displayFriends(friends);


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FindFriends.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void displayFriends(List<User> friends) {
        rvFriends.setLayoutManager(new LinearLayoutManager(FindFriends.this));
        friendAdapter=new FriendAdapter(FindFriends.this,friends,users);
        rvFriends.setAdapter(friendAdapter);
    }
}