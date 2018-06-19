package com.example.emman.crimereport.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emman.crimereport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginBtn;
    private TextView mCreateAccount;
    private RelativeLayout mLoginLayout;
    private EditText mSignupEmail;
    private EditText mSignupPassword;
    private Button mSignupBtn;
    private TextView mLoginAccount;
    private RelativeLayout mSignupLayout;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginEmail = findViewById(R.id.login_email_text);
        mLoginPassword = findViewById(R.id.login_password_text);
        mLoginBtn = findViewById(R.id.login_btn);
        mCreateAccount = findViewById(R.id.create_link);
        mSignupEmail = findViewById(R.id.signup_email_text);
        mSignupPassword = findViewById(R.id.signup_password_text);
        mSignupBtn = findViewById(R.id.signup_btn);
        mLoginAccount = findViewById(R.id.login_link);

        mLoginLayout = findViewById(R.id.login_layout);
        mSignupLayout = findViewById(R.id.signup_layout);


        mProgressDialog =new ProgressDialog(this);

        mSignupLayout.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();
                LoginUsers(email,password);
                mProgressDialog.setTitle("Connecting...");
                mProgressDialog.setMessage("Connecting with server");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

            }
        });

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mSignupEmail.getText().toString();
                String password = mSignupPassword.getText().toString();
                Signup(email,password);
            }
        });

        mCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginLayout.setVisibility(View.GONE);
                mSignupLayout.setVisibility(View.VISIBLE);
            }
        });

        mLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginLayout.setVisibility(View.VISIBLE);
                mSignupLayout.setVisibility(View.GONE);
            }
        });
    }

    private void LoginUsers (String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public static final String TAG = "login" ;

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });


    }

    private void updateUI(Object user) {

        if(user != null ){
            mProgressDialog.dismiss();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        } if (user == null) {
            mProgressDialog.dismiss();
        }

    }

    private void Signup (String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public static final String TAG = "Signup" ;
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            mLoginLayout.setVisibility(View.VISIBLE);
                            mSignupLayout.setVisibility(View.GONE);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


}

