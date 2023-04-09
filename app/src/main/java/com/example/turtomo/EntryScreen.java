package com.example.turtomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class EntryScreen extends AppCompatActivity {

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(EntryScreen.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }else{
            loginButton= findViewById(R.id.loginRedirectButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EntryScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

}