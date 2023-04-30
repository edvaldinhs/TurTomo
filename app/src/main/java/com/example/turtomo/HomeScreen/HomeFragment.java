package com.example.turtomo.HomeScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turtomo.HomeScreen.HomeFrag.Block;
import com.example.turtomo.HomeScreen.HomeFrag.CustomAdapter;
import com.example.turtomo.HomeScreen.RoomFrag.Room;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    ArrayList<Block> blocks = new ArrayList<>();
    ListView listView;


    NavController navController;

    private TextView helloMessage;
    private FirebaseAuth firebaseAuth;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        navController = Navigation.findNavController(requireActivity(), R.id.frame_layout);
        listView = (ListView)view.findViewById(R.id.listViewBlock);
        helloMessage = view.findViewById(R.id.helloMessage);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        fillBlockValue();

        return view;
    }

    private void checkUser() {
        //verifica se está realmente logado
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(getActivity(), EntryScreen.class));
            getActivity().finish();
        }else {
            //pega a info do User
            if(firebaseUser.getDisplayName()!=null){
                String name = firebaseUser.getDisplayName();
                helloMessage.setText("Hello\n"+name);
            }
        }
    }

    public void fillListView(){
        CustomAdapter myCustomAdapter = new CustomAdapter(getActivity(), blocks, navController);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillBlockValue(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");
        Query query = reference.orderByChild("blockId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blocks.clear();

                for (DataSnapshot blockSnapshot : dataSnapshot.getChildren()) {
                                String blockId = blockSnapshot.child("blockId").getValue(String.class);
                                String blockName = blockSnapshot.child("blockName").getValue(String.class);
                                Block b = new Block(blockId, blockName);

                    int roomFirstNumber = Integer.MAX_VALUE;
                    int roomLastNumber = Integer.MIN_VALUE;


                    for (DataSnapshot roomSnapshot : blockSnapshot.getChildren()) {
                        if (roomSnapshot.getKey().startsWith("room")) {
                            int roomNumber = roomSnapshot.child("roomNumber").getValue(Integer.class);
                            if (roomNumber < roomFirstNumber) {
                                roomFirstNumber = roomNumber;
                            }
                            if (roomNumber > roomLastNumber) {
                                roomLastNumber = roomNumber;
                            }
                        }
                    }
                    b.setPrimeiraSala(roomFirstNumber);
                    b.setUltimaSala(roomLastNumber);

                                blocks.add(b);
                }

                fillListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Block Database organizer Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}