package com.example.turtomo.HomeScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.turtomo.Login.EntryScreen;
import com.example.turtomo.R;
import com.example.turtomo.databinding.ActivityHomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeScreen extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private FirebaseAuth firebaseAuth;

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initNavigation();

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

    }

    private void checkUser() {
        //verifica se está realmente logado
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this, EntryScreen.class));
            finish();
        }
    }

    private void initNavigation(){
        navHostFragment
                = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }
}