package com.example.turtomo.HomeScreen.Connection;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.turtomo.HomeScreen.HomeScreen;
import com.example.turtomo.HomeScreen.RoomFrag.CaptureAct;
import com.example.turtomo.Login.EntryScreen;
import com.example.turtomo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Connection extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText connectEditText;
    private Button confirmConnectBtn;
    private Button qrConnectBtn;
    private ImageView qrImageConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        connectEditText = findViewById(R.id.connectEditText);
        firebaseAuth = FirebaseAuth.getInstance();

        confirmConnectBtn = findViewById(R.id.bt_connect);
        confirmConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEntityCode(connectEditText.getText().toString());
            }
        });
        qrConnectBtn = findViewById(R.id.qrconnection_btn3);
        qrImageConnectBtn = findViewById(R.id.qrImageView);

        qrConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
        qrImageConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

    }

    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume cima para ligar o flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {
            CheckEntityCode(result.getContents());
        }
    });

    public void CheckEntityCode(String codeInEditText){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && !codeInEditText.isEmpty()) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.orderByChild("users");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot refSnapshot : snapshot.getChildren()) {
                        boolean isAlreadyConnected = false;
                        for (DataSnapshot userSnapshot : refSnapshot.child("users").getChildren()) {
                            if (firebaseUser.getEmail().equals(userSnapshot.child("userEmail").getValue(String.class) + "")) {
                                isAlreadyConnected = true;
                            }
                        }
                        if(!isAlreadyConnected && codeInEditText.equals(refSnapshot.child("idEntity").getValue(String.class).toUpperCase())){
                            String userKey = refSnapshot.child("users").getRef().push().getKey();
                            refSnapshot.child("users").getRef().child(userKey+"/userEmail").setValue(firebaseUser.getEmail());
                            refSnapshot.child("users").getRef().child(userKey+"/userName").setValue(firebaseUser.getDisplayName());
                            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    }
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
}