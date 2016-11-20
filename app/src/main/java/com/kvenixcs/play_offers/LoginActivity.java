package com.kvenixcs.play_offers;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Intent;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "PLAY-OFFers::";
    private FirebaseAuth mAuth;

    TextView emailTextView, passwordTextView;
    Button loginButton, registerButton;

    ProgressDialog progressDialog;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        emailTextView = (TextView) findViewById(R.id.EmailtEditText);
        passwordTextView = (TextView) findViewById(R.id.EmailtEditText);
        loginButton = (Button) findViewById(R.id.LoginButton);
        registerButton = (Button) findViewById(R.id.RegisterButton);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    void onClickLogin(View view)
    {
        final Intent intent = new Intent(this,MainActivity.class);
        progressDialog = new ProgressDialog(this);
        //progressDoalog.setMax(100);
        progressDialog.setMessage("Carregando ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();



        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(emailTextView.toString(), passwordTextView.toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.cancel();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());

                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();

                            // Setting Dialog Title
                            alertDialog.setTitle("Mensagem");

                            // Setting Dialog Message
                            alertDialog.setMessage("Erro!");

                            // Showing Alert Message
                            alertDialog.show();
                        }

                        // ...
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(intent);
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


    }





    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
