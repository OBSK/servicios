package com.appservicios.arturo.appservicios.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.appservicios.arturo.appservicios.Object.User;
import com.appservicios.arturo.appservicios.R;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class startActivity extends AppCompatActivity
        private Firebase rootUrl;
        private AuthData mAuthData;
        TextView tvUsserName;
        private Firebase.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tvUsserName=(TextView)findViewById(R.id.tvUsserName);

        private ValueEventListener valueEventListenerCurrenUser = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvUsserName.setText("Hola" + user.name);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        }
    }
