package com.example.turtomo.HomeScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turtomo.HomeScreen.RoomFrag.CustomAdapter;
import com.example.turtomo.R;
import com.example.turtomo.HomeScreen.RoomFrag.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RoomsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<Room> rooms = new ArrayList<>();
    ListView listView;
    private EditText searchEditText;
    private Button search;

    public RoomsFragment() {
        // Required empty public constructor
    }


    public static RoomsFragment newInstance(String param1, String param2) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        searchEditText = view.findViewById(R.id.searchEditTextRoom);
        search = view.findViewById(R.id.search);

        String searchResults = searchEditText.getText().toString();

        fillRoomValue(searchResults);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchResults = searchEditText.getText().toString();
                rooms.clear();
                fillRoomValue(searchResults);
            }
        });
        return view;
    }

    public void fillListView(){
        CustomAdapter myCustomAdapter = new CustomAdapter(getActivity(), rooms);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillRoomValue(String searchResults){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");
        Query query = reference.orderByChild("blockId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot blockSnapshot : dataSnapshot.getChildren()) {
                    String blockId = blockSnapshot.child("blockId").getValue().toString();
                    for (DataSnapshot roomSnapshot : blockSnapshot.getChildren()) {
                        if (roomSnapshot.getKey().startsWith("room")) {
                            if (searchResults.isEmpty() ||
                                    roomSnapshot.child("roomNumber").getValue(Integer.class).toString().toLowerCase().contains(searchResults.toLowerCase())
                                    || "Sala".toLowerCase().contains(searchResults.toLowerCase())) {
                                String id = roomSnapshot.child("roomId").getValue(String.class);
                                int roomNumber = roomSnapshot.child("roomNumber").getValue(Integer.class);
                                Room r = new Room(id, roomNumber, blockId);
                                rooms.add(r);
                            }
                        }
                    }
                }

                fillListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Room Database organizer Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



}