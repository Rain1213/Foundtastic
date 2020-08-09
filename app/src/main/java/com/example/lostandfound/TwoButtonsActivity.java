package com.example.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TwoButtonsActivity extends AppCompatActivity {
  private Button myLostButton;
  private Button myFoundButton;

  private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_buttons_layout);

        myLostButton=(Button)findViewById(R.id.LostButton);
        myFoundButton=(Button)findViewById(R.id.FoundButton);

        mAuth=FirebaseAuth.getInstance();

        myLostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TwoButtonsActivity.this,LostPageActivity.class);
                intent.putExtra("from","LostButton");
                startActivity(intent);
                finish();
            }
        });
        myFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TwoButtonsActivity.this,LostPageActivity.class);
                intent.putExtra("from","FoundButton");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.action_signout){
             mAuth.signOut();
             startActivity(new Intent(TwoButtonsActivity.this,MainActivity.class));
             finish();
        }

        if(item.getItemId()== R.id.action_myaccount){
            startActivity(new Intent(TwoButtonsActivity.this,MyAccountActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
