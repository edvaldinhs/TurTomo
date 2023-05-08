package com.example.turtomo.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import com.example.turtomo.HomeScreen.HomeScreen;
import com.example.turtomo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.annotation.Nonnull;

public class EntryScreen extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    private Button mSignInButton;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    static final String TAG = "EntryScreen_SignIn";

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);
        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(EntryScreen.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            mGoogleSignInClient.signOut();

            loginButton= findViewById(R.id.loginRedirectButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EntryScreen.this, Login.class);
                    startActivity(intent);
                }
            });

            mSignInButton = findViewById(R.id.googleSignIn);

            mAuth = FirebaseAuth.getInstance();

            mSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@Nonnull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(EntryScreen.this, HomeScreen.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Do your login failure handling here
                        }
                    }
                });
    }

}