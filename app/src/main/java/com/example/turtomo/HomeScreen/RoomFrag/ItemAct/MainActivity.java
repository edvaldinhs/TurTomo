package com.example.turtomo.HomeScreen.RoomFrag.ItemAct;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turtomo.HomeScreen.RoomFrag.CaptureAct;
import com.example.turtomo.HomeScreen.RoomFrag.ItemAct.CustomAdapterItem;
import com.example.turtomo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Bundle extras;
    int roomNumber;


    ArrayList<Item> items = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        extras = getIntent().getExtras();
        roomNumber = extras.getInt("roomNumber");

        listView = (ListView)findViewById(R.id.listViewItem);

        fillItemValue();
        findViewById(R.id.fab2).setOnClickListener(view -> {
            scanCode();
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
            checkItemPresence(result.getContents());
        }
    });

    public void fillListView(){
        CustomAdapterItem myCustomAdapter = new CustomAdapterItem(MainActivity.this, items);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillItemValue(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rooms")
                .child("room"+roomNumber).child("items");
        Query query = reference.orderByChild("itemId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String itemId = snapshot.child("itemId").getValue(String.class);
                    String itemName = snapshot.child("itemName").getValue(String.class);
                    boolean check = snapshot.child("check").getValue(boolean.class);
                    Item item = new Item(itemId, itemName, check);
                    items.add(item);
                }
                fillListView();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Item Database organizer Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkItemPresence(String tomo){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rooms")
                .child("room"+roomNumber).child("items");
        Query query = reference.orderByChild("itemId").equalTo(tomo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference userRef = snapshot.getRef();
                    userRef.child("check").setValue(true);
                    for (Item item : items){
                        if(item.getTomoId().equals(tomo)){
                            item.setCheck(true);
                        }
                    }
                }
                fillListView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Item Database organizer Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}