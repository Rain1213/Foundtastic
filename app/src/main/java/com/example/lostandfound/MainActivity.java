package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    private EditText mUserEmail;
    private EditText mUserPassword;
    private Button mUserLoginButton;
    private TextView mUserLoginErrorText;
    private Button mCreateActButton;

    private long backPressedTime;
    private Toast backToast;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    OnlyEmail obj = new OnlyEmail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserEmail=(EditText)findViewById(R.id.UserEmail);
        mUserPassword=(EditText)findViewById(R.id.UserPassword);
        mUserLoginButton=(Button)findViewById(R.id.UserLoginButton);
        mUserLoginErrorText=(TextView)findViewById(R.id.UserLoginError);
        mCreateActButton=(Button)findViewById(R.id.CreateActButton);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        FirebaseAuth.getInstance().signOut();
        mAuth=FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Toast.makeText(MainActivity.this, "You are Logged In",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }
        };

        mUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String userEmail=mUserEmail.getText().toString();
                String userPwd=mUserPassword.getText().toString();

                if(userEmail.isEmpty()){
                    mUserEmail.setError("Please Enter Email");
                    mUserEmail.requestFocus();
                }

                if(userPwd.isEmpty()){
                    mUserPassword.setError("Please Enter Password");
                    mUserPassword.requestFocus();
                }

                if(userEmail.isEmpty() && userPwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields Are Empty",
                            Toast.LENGTH_SHORT).show();
                }

                if(!(userEmail.matches(emailPattern))){
                    mUserEmail.setError("Invalid Email");
                    mUserEmail.requestFocus();
                }

                if(!(userEmail.isEmpty() && userPwd.isEmpty()) && userEmail.matches(emailPattern)){
                    mAuth.signInWithEmailAndPassword(userEmail,userPwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        if(mAuth.getCurrentUser().isEmailVerified()){
                                            FirebaseUser currentUser=mAuth.getCurrentUser();
                                            String email = currentUser.getEmail();
                                            obj.setEmail(email);

                                            // Sign in success, update UI with the signed-in user's information
                                            startActivity(new Intent(MainActivity.this,TwoButtonsActivity.class));
                                            finish();
                                        }
                                        else{
                                            Log.d("Verify Email","Not verified");
                                            Toast.makeText(MainActivity.this, "Please verify your email address",
                                                    Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                                        }
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Failed", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        mUserLoginErrorText.setVisibility(View.VISIBLE);
                                    }
                                    // ...
                                }
                            });
                }
            }
        });

        mCreateActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
            }
        });
    }

    public void onBackPressed(){
        /*Intent i=new Intent(MainActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();*/
        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        else{
            backToast=Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
