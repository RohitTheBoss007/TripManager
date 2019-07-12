package com.example.android.tripmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Places extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        Toolbar toolbar = findViewById(R.id.toolbarPlaces);
        setSupportActionBar(toolbar);
        ImageView img=findViewById(R.id.imgplaces);
        Glide.with(this).load("https://images.unsplash.com/photo-1528999101658-2ed042724a1f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
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
    }
}
