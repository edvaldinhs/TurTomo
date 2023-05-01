package com.example.turtomo.HomeScreen;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.turtomo.HomeScreen.RoomFrag.BlockSearch;
import com.example.turtomo.HomeScreen.RoomFrag.CustomAdapter;
import com.example.turtomo.HomeScreen.RoomFrag.ItemSpacingDecoration;
import com.example.turtomo.HomeScreen.RoomFrag.SearchBlockCustomAdapter;
import com.example.turtomo.R;
import com.example.turtomo.HomeScreen.RoomFrag.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.annotation.Nonnull;


public class RoomsFragment extends Fragment implements SearchBlockCustomAdapter.OnBlockClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<BlockSearch> block_searchs = new ArrayList<>();

    Bundle extras;
    ListView listView;
    RecyclerView searchRecyclerView;
    private EditText searchEditText;
    private Button search;
    private String searchByBlockId = "";
    private SearchBlockCustomAdapter mySearchCustomAdapter;

    public RoomsFragment() {
        // Required empty public constructor
    }

    @Nonnull
    @Override
    public void onBlockClick(String blockId) {
        // Handle the block click event here
        // You can use the blockId parameter to perform any action you need
//        Toast.makeText(getActivity(), "Block " + blockId + " clicked", Toast.LENGTH_SHORT).show();

        // Update the searchByBlockId variable
        searchByBlockId = blockId;
        // Reload the room data based on the selected block
        String searchResults = searchEditText.getText().toString();
        rooms.clear();
        if(searchByBlockId!=null){
            fillRoomValue(searchResults, searchByBlockId);
        }
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
        extras = getArguments();
        boolean isFromPHome = false;
        if(extras!=null){
            searchByBlockId = extras.getString("blockId");
            isFromPHome = true;


        }
        // Inflate the layout for this fragment
        View view;
        if(isFromPHome){
            view = inflater.inflate(R.layout.fragment_rooms_from_home, container, false);
            try {
                Button goBackHome = view.findViewById(R.id.goBack);

                goBackHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.frame_layout);
                        if (navController != null) {
                            navController.navigate(R.id.action_rooms_from_home_to_home);
                        } else {
                            Log.e("CustomAdapter", "NavController is null");
                        }
                    }
                });
            }catch(Exception e){

            }
        }else{
            view = inflater.inflate(R.layout.fragment_rooms, container, false);
        }


        listView = (ListView)view.findViewById(R.id.listView);



        searchRecyclerView = view.findViewById(R.id.searchListView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        int spacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        ItemSpacingDecoration itemSpacingDecoration = new ItemSpacingDecoration(spacing);
        searchRecyclerView.addItemDecoration(itemSpacingDecoration);

        searchEditText = view.findViewById(R.id.searchEditTextRoom);
        search = view.findViewById(R.id.search);

        String searchResults = searchEditText.getText().toString();

        fillBlockValue(isFromPHome, searchByBlockId);
        fillRoomValue(searchResults, searchByBlockId);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchResults = searchEditText.getText().toString();
                rooms.clear();
                fillRoomValue(searchResults, searchByBlockId);
            }
        });


        return view;
    }
    public void setCheck(String blockId){
        if(!blockId.isEmpty() || !block_searchs.isEmpty()){
            for (BlockSearch b : block_searchs){
                if(b.getBlockId().toLowerCase().equals(blockId)){
                    b.setSelected(true);
                }else {
                    b.setSelected(false);
                }
            }
            fillSearchListView();
        }
    }

    public void fillListView(){
        CustomAdapter myCustomAdapter = new CustomAdapter(getActivity(), rooms);
        listView.setAdapter(myCustomAdapter);
    }

    public void fillRoomValue(String searchResults, String searchByBlockId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");
        Query query = reference.orderByChild("blockId");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot blockSnapshot : dataSnapshot.getChildren()) {
                    String blockId = blockSnapshot.child("blockId").getValue().toString();
                    if (searchByBlockId.isEmpty() ||
                            blockSnapshot.child("blockId").getValue(String.class).toLowerCase().contains(searchByBlockId.toLowerCase())) {
                    for (DataSnapshot roomSnapshot : blockSnapshot.getChildren()) {
                        if (searchResults.isEmpty() ||
                                roomSnapshot.child("roomNumber").getValue(Integer.class).toString().toLowerCase().contains(searchResults.toLowerCase())
                                || "Sala".toLowerCase().contains(searchResults.toLowerCase())) {

                            //Verify the rooms by the searchBar
                            if (roomSnapshot.getKey().startsWith("room")) {


                                if (searchResults.isEmpty() ||
                                        roomSnapshot.child("roomNumber").getValue(Integer.class).toString().
                                                toLowerCase().contains(searchResults.toLowerCase())) {

                                    String id = roomSnapshot.child("roomId").getValue(String.class);
                                    int roomNumber = roomSnapshot.child("roomNumber").getValue(Integer.class);
                                    Room r = new Room(id, roomNumber, blockId);
                                    rooms.add(r);

                                }
                            }
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
    private void fillSearchListView(){
        SearchBlockCustomAdapter mySearchCustomAdapter = new SearchBlockCustomAdapter(getActivity(), block_searchs, this);
        searchRecyclerView.setAdapter(mySearchCustomAdapter);
    }

    public void fillBlockValue(boolean isFromPHome, String blockIde){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blocks");
        Query query = reference.orderByChild("blockId");

        block_searchs.add(new BlockSearch("","All", true));

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot blockSnapshot : dataSnapshot.getChildren()) {
                    String blockId = blockSnapshot.child("blockId").getValue().toString();
                    String blockName = blockSnapshot.child("blockName").getValue(String.class);
                    BlockSearch b = new BlockSearch(blockId, blockName, false);
                    block_searchs.add(b);
                }

                fillSearchListView();
                if(isFromPHome){
                    setCheck(blockIde);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Room Database organizer Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}