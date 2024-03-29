package com.example.turtomo.HomeScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.turtomo.HomeScreen.Connection.Entity;
import com.example.turtomo.HomeScreen.Connection.EntityViewModel;
import com.example.turtomo.Login.EntryScreen;
import com.example.turtomo.R;
import com.example.turtomo.databinding.ActivityHomeScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeScreen extends AppCompatActivity {

    private ActivityHomeScreenBinding binding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private FirebaseAuth firebaseAuth;
    private Entity entity = new Entity();
    private EntityViewModel entityViewModel;
    private Bundle bundle = new Bundle();


    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        entityViewModel = new ViewModelProvider(this).get(EntityViewModel.class);
        entityViewModel.setEntity(entity);

        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkEntity();
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

    private void checkEntity() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.orderByChild("users");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot refSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot userSnapshot : refSnapshot.child("users").getChildren()) {
                            if (firebaseUser.getEmail().equals(userSnapshot.child("userEmail").getValue(String.class) + "")) {
                                entity.setName(refSnapshot.getKey());
                                entity.setEntityID(refSnapshot.child("idEntity").getValue(String.class));
                                bundle.putString("enameb", refSnapshot.getKey());
                            }
                        }
                    }
                    initNavigation();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });


            if (firebaseUser.getEmail() + "" != null && firebaseUser.getEmail().equals("h")) {
                startActivity(new Intent(this, EntryScreen.class));
                finish();
            }
        }
    }

    private void initNavigation(){
        navHostFragment
                = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        navController = navHostFragment.getNavController();
        navController.setGraph(R.navigation.nav_main, bundle);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
    }
}