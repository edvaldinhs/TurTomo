package com.example.turtomo.HomeScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.turtomo.HomeScreen.RoomFrag.CustomAdapter;
import com.example.turtomo.R;
import com.example.turtomo.HomeScreen.RoomFrag.Room;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<Room> rooms = new ArrayList<>();
    Button createRoom_btn;
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

        createRoom_btn = view.findViewById(R.id.createRoom);
        listView = (ListView)view.findViewById(R.id.listView);

        createRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildRoomAL();
                fillListView();
            }
        });
        return view;
    }

    public void buildRoomAL(){
        Room r = new Room();
        String id = "00001";
        int roomNumber = 1;
        r = new Room(id, roomNumber);
        rooms.add(r);

    }
    public void fillListView(){
        CustomAdapter myCustomAdapter = new CustomAdapter(getActivity(), rooms);
        listView.setAdapter(myCustomAdapter);
    }
}