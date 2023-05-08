package com.example.turtomo.HomeScreen.RoomFrag.ItemAct;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turtomo.HomeScreen.RoomFrag.CaptureAct;
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

public class MainActivity extends AppCompatActivity {

    Bundle extras;
    String entityName;
    int roomNumber;
    int blockNumber;
    private EditText searchEditText;

    private Button reset;
    private Button search;

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
        blockNumber = Integer.parseInt(extras.getString("blockId"));
        entityName = extras.getString("entityName");

        listView = (ListView)findViewById(R.id.listViewItem);
        reset = findViewById(R.id.reset);

        searchEditText = (EditText) findViewById(R.id.searchEditTextItem);
        String searchResults = searchEditText.getText().toString();
        search = findViewById(R.id.search3);

        fillItemValue(searchResults, blockNumber);
        findViewById(R.id.fab2).setOnClickListener(view -> {
            scanCode();
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchResults = searchEditText.getText().toString();
                items.clear();
                fillItemValue(searchResults, blockNumber);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("ATENÇÃO");
                builder.setMessage("Confirmar irá resetar o estado de presença de TODOS os itens. Continuar?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelItemPresence();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();;
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
            checkItemPresence(result.getContents());
        }
    });

    public void fillListView(){
        CustomAdapterItem myCustomAdapter = new CustomAdapterItem(MainActivity.this, items);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillItemValue(String searchResults, int blockNumber){
        DatabaseReference reference;
        if(!entityName.isEmpty()) {
            if (roomNumber < 10 && roomNumber >= 0) {
                reference = FirebaseDatabase.getInstance().getReference(entityName+"/blocks")
                        .child("block" + blockNumber).child("room0" + roomNumber).child("items");
            } else {
                reference = FirebaseDatabase.getInstance().getReference(entityName+"/blocks")
                        .child("block" + blockNumber).child("room" + roomNumber).child("items");
            }
            Query query = reference.orderByChild("itemId");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (searchResults.isEmpty()) {
                            String itemId = snapshot.child("itemId").getValue(String.class);
                            String itemName = snapshot.child("itemName").getValue(String.class);
                            boolean check = snapshot.child("check").getValue(boolean.class);
                            Item item = new Item(itemId, itemName, check);
                            items.add(item);
                        } else {
                            if (snapshot.child("itemName").getValue(String.class).toLowerCase().contains(searchResults.toLowerCase())) {
                                String itemId = snapshot.child("itemId").getValue(String.class);
                                String itemName = snapshot.child("itemName").getValue(String.class);
                                boolean check = snapshot.child("check").getValue(boolean.class);
                                Item item = new Item(itemId, itemName, check);
                                items.add(item);
                            }
                        }
                    }
                    fillListView();
                }

                public void search() {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Item Database organizer Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
    public void cancelItemPresence(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rooms")
                .child("room"+roomNumber).child("items");
        Query query = reference.orderByChild("itemId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference userRef = snapshot.getRef();
                    userRef.child("check").setValue(false);
                    for (Item item : items){
                            item.setCheck(false);
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