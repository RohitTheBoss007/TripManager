package com.example.android.tripmanager;


import com.google.firebase.database.DataSnapshot;
import  android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/* Created by Rohit Pal on 24-06-2019 */


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_SIGNUP = 0;
    //FirebaseDatabase database;
    //DatabaseReference users;
    DatabaseReference ref;

    //DatabaseReference users1 = users.child("Rohit");

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ref=FirebaseDatabase.getInstance().getReference().child("Users");
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        _emailText=(EditText)findViewById(R.id.input_email);
        _loginButton=(Button) findViewById(R.id.btn_login);
        _signupLink=(TextView) findViewById(R.id.link_signup);
        _passwordText=(EditText)findViewById(R.id.input_password);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View v) {
                // Start the SignUp activity
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void login() {
        Log.d(TAG, "Login");

        _loginButton.setEnabled(false);
        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        _loginButton.setEnabled(true);
                        //finish();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);



        // TODO: Implement your own authentication logic here.

        final String email = EncodeString(_emailText.getText().toString());
        final String password = _passwordText.getText().toString();
        Log.d(TAG, email);
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("INFO",String.valueOf(dataSnapshot)) ;
                if(dataSnapshot.exists())
                {
                    Query query = ref.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String rface = (String) child.child("password").getValue();
                                if (rface.equals(password) && !rface.equals("")) {
                                    User login=new User();
                                    login.setUsername((String)child.child("username").getValue());
                                    login.setEmail((String)child.child("email").getValue());
                                    login.setAddress((String)child.child("address").getValue());
                                    login.setMobile((String)child.child("mobile").getValue());
                                    login.setPassword((String)child.child("password").getValue());
                                    Common.currentUser=login;
                                    startActivity(new Intent(MainActivity.this, Final.class));
                                } else{
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();}
                            }
                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

