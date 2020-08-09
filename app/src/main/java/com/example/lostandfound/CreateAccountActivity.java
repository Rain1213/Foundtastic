package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private Button mCreateActSignUpBtn;
    private EditText mCreateActEmail;
    private EditText mCreateActPassword;
    private EditText mCreateActConfirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mCreateActConfirmPassword=(EditText)findViewById(R.id.CreateActConfirmPassword);
        mCreateActEmail=(EditText)findViewById(R.id.CreateActEmail);
        mCreateActPassword=(EditText)findViewById(R.id.CreateActPassword);
        mCreateActSignUpBtn=(Button)findViewById(R.id.CreateActSignUpBtn);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        mAuth=FirebaseAuth.getInstance();

        //FirebaseUser currentUser=mAuth.getCurrentUser();
        //String email=currentUser.getEmail();

        mCreateActSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserEmail=mCreateActEmail.getText().toString();
                String newUserPwd=mCreateActPassword.getText().toString();
                String newUserCfmPwd=mCreateActConfirmPassword.getText().toString();

                    if (!(newUserCfmPwd.equals(newUserPwd))) {
                        mCreateActConfirmPassword.setError("Please Enter Password SAME as above");
                        mCreateActConfirmPassword.requestFocus();
                    }
                    if (newUserEmail.isEmpty()) {
                        mCreateActEmail.setError("Please Enter Valid Email ID");
                        mCreateActEmail.requestFocus();
                    }
                    if (newUserPwd.isEmpty()) {
                        mCreateActEmail.setError("Please Enter Password");
                        mCreateActEmail.requestFocus();
                    }
                    if (newUserCfmPwd.isEmpty()) {
                        mCreateActEmail.setError("Please Enter Password SAME as above");
                        mCreateActEmail.requestFocus();
                    }
                    if (newUserEmail.isEmpty() && newUserPwd.isEmpty() && newUserCfmPwd.isEmpty()) {
                        Toast.makeText(CreateAccountActivity.this, "Fields Are EMPTY", Toast.LENGTH_SHORT).show();
                    }
                    if(!(newUserEmail.matches(emailPattern))){
                        mCreateActEmail.setError("Please Enter Valid Email ID");
                        mCreateActEmail.requestFocus();
                    }
                    if (!(newUserEmail.isEmpty() && newUserPwd.isEmpty() && newUserCfmPwd.isEmpty()) && newUserEmail.matches(emailPattern)) {
                       mAuth.createUserWithEmailAndPassword(newUserEmail,newUserCfmPwd)
                               .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull Task<AuthResult> task) {
                                       if(task.isSuccessful()){
                                           mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()){
                                                       Toast.makeText(CreateAccountActivity.this,
                                                               "Registered Successfully. Please check your email for verification ",
                                                               Toast.LENGTH_LONG).show();
                                                       finish();
                                                   }
                                                   else{
                                                       Toast.makeText(CreateAccountActivity.this,task.getException().getMessage(),
                                                               Toast.LENGTH_LONG).show();
                                                   }
                                               }
                                           });
                                           Toast.makeText(CreateAccountActivity.this, "Account Created",
                                                   Toast.LENGTH_SHORT).show();
                                           startActivity(new Intent(CreateAccountActivity.this,
                                                   MainActivity.class));
                                       }
                                       if(!task.isSuccessful()){
                                           Toast.makeText(CreateAccountActivity.this, "Account Creation FAILED.Please try again",
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                    }



            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(CreateAccountActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}
