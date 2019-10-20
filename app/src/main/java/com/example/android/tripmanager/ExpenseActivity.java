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
import com.example.android.tripmanager.Adapter.ExpenseAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    String tripName;
    RecyclerView rvxp;
    List<Expense>expenses=new ArrayList<>();
    ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = findViewById(R.id.xtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Expense Summary");
        db=FirebaseDatabase.getInstance();
        Intent k=getIntent();
        tripName=k.getStringExtra("TripName");
        rvxp=findViewById(R.id.rvxp);
        ImageView img=findViewById(R.id.imgbackx);
        Glide.with(this).load("https://images.pexels.com/photos/542347/pexels-photo-542347.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expensemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addxp:
                AddExpense();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddExpense() {
        Intent intent=new Intent(ExpenseActivity.this,AddExpense.class);
        intent.putExtra("TripName",tripName);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        expenses.clear();
        ref=db.getReference("expenses");
        ref.orderByChild("tripName").equalTo(tripName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Expense x=dataSnapshot.getValue(Expense.class);
                expenses.add(x);
                displayExpenses(expenses);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                List<Expense>newExpenses=new ArrayList<>();
                Expense x=dataSnapshot.getValue(Expense.class);
                newExpenses.add(x);
                expenses=newExpenses;
                displayExpenses(expenses);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                List<Expense>newExpenses=new ArrayList<>();
                Expense x=dataSnapshot.getValue(Expense.class);
                newExpenses.add(x);
                expenses=newExpenses;
                displayExpenses(expenses);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayExpenses(List<Expense> expenses) {
        rvxp.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
        expenseAdapter=new ExpenseAdapter(ExpenseActivity.this,expenses,ref);
        rvxp.setAdapter(expenseAdapter);
    }
}
