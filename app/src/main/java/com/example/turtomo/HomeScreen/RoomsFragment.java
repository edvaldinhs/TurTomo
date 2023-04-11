package com.example.turtomo.HomeScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        fillRoomValue();
        return view;
    }

    public void fillListView(){
        CustomAdapter myCustomAdapter = new CustomAdapter(getActivity(), rooms);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillRoomValue(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("rooms");
        Query query = reference.orderByChild("roomId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String id = snapshot.child("roomId").getValue(String.class);
                    int roomNumber = snapshot.child("roomNumber").getValue(Integer.class);
                    Room r = new Room(id, roomNumber);
                    rooms.add(r);
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