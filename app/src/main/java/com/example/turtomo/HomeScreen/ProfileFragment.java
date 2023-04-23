package com.example.turtomo.HomeScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.turtomo.Login.EntryScreen;
import com.example.turtomo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth firebaseAuth;


    private TextView btn;
    private CircleImageView photoP;
    private TextView nameP;
    private TextView emailP;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        photoP = (CircleImageView) view.findViewById(R.id.photoProfile);
        nameP = view.findViewById(R.id.nameProfile);
        emailP = view.findViewById(R.id.emailProfile);
        emailP.setPaintFlags(emailP.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        btn = view.findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                mGoogleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();
                checkUser();
            }
        });

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
            String email = firebaseUser.getEmail();
            emailP.setText(email);
            if(firebaseUser.getPhotoUrl()!=null){
                String photoUrl = firebaseUser.getPhotoUrl().toString();
                Glide.with(getContext())
                        .load(photoUrl)
                        .into(photoP);
            }
            if(firebaseUser.getDisplayName()!=null){
                String name = firebaseUser.getDisplayName();
                nameP.setText(name);
            }
        }
    }
}