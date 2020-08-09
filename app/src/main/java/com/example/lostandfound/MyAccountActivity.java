package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.RVAdapter;
import Model.RVListItem;

public class MyAccountActivity extends AppCompatActivity{

    private MainActivity obj1 = new MainActivity();
    private RecyclerView recyclerView;
    private RVAdapter adapter;
    private List<LauncherActivity.ListItem> listItems;

    public String LostEmail;
    public String FoundEmail;

    public int flagl=0,flagf=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        recyclerView=findViewById(R.id.MyAccountRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems=new ArrayList<>();

            DatabaseReference mainReferenceL = FirebaseDatabase.getInstance().getReference("LostObjectActivity");
            final DatabaseReference checkLReference=FirebaseDatabase.getInstance().getReference("Lost_Found");

            mainReferenceL.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot mainReferenceSnapshot) {

                    if (mainReferenceSnapshot.exists()) {
                        for (final DataSnapshot mainItemSnapshot : mainReferenceSnapshot.getChildren()) {
                            final String lEmail=String.valueOf(mainItemSnapshot.child("email").getValue());
                            final String ItemType=String.valueOf(mainItemSnapshot.child("type").getValue());
                            final String CompanyName=String.valueOf(mainItemSnapshot.child("companyName").getValue());
                            final String checkInMain= String.valueOf(mainItemSnapshot.child("check").getValue());

                            //----------------checkReference Section------------------------------------------------------//
                            checkLReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot checkReferenceSnapshot) {
                                    if(checkReferenceSnapshot.exists()) {
                                        for(DataSnapshot checkItemSnapshot:checkReferenceSnapshot.getChildren()){
                                            String checkInMain=String.valueOf(mainItemSnapshot.child("check").getValue());
                                            String detailInLostFound=String.valueOf(checkItemSnapshot.child("detail").getValue());
                                            String lostEmailInLostFound=String.valueOf(checkItemSnapshot.child("lostEmail").getValue());

                                            /*if(checkInMain.equals(detailInLostFound) && lEmail.equals(lostEmailInLostFound)){
                                                flagl=1;
                                                FoundEmail=String.valueOf(checkItemSnapshot.child("foundEmail").getValue());
                                                break;
                                            }*/

                                            if(checkInMain.equals(detailInLostFound) && lEmail.equals(lostEmailInLostFound)){
                                                FoundEmail=String.valueOf(checkItemSnapshot.child("foundEmail").getValue());
                                                RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: "+FoundEmail,checkInMain);
                                                if(lEmail.equals(obj1.obj.getEmail())){
                                                    flagl=1;
                                                    listItems.add(i);
                                                }
                                                adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                                recyclerView.setAdapter(adapter);
                                                break;
                                            }
                                        }
                                        if(flagl==0){
                                            RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: ",checkInMain);
                                            if(lEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        /*/////////////////////////////////////////////////////////////////////////////////////
                                        if(flagl==1 && !(FoundEmail.equals(obj1.obj.getEmail()))){
                                            RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: "+FoundEmail);
                                            if(lEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        else{
                                            RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: ");
                                            if(lEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        /////////////////////////////////////////////////////////////////////////////////////*/
                                    }
                                    else{
                                        RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: ","");
                                        if(lEmail.equals(obj1.obj.getEmail())){
                                            listItems.add(i);
                                        }
                                        adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            flagl=0;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference mainReferenceF= FirebaseDatabase.getInstance().getReference("FoundObjectActivity");
            final DatabaseReference checkFReference=FirebaseDatabase.getInstance().getReference("Lost_Found");

            mainReferenceF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot mainReferenceSnapshot) {

                    if (mainReferenceSnapshot.exists()) {
                        for (final DataSnapshot mainItemSnapshot : mainReferenceSnapshot.getChildren()) {

                            final String fEmail=String.valueOf(mainItemSnapshot.child("email").getValue());
                            final String ItemType=String.valueOf(mainItemSnapshot.child("type").getValue());
                            final String CompanyName=String.valueOf(mainItemSnapshot.child("companyName").getValue());
                            final String checkInMain=String.valueOf(mainItemSnapshot.child("check").getValue());

                            //----------------checkReference Section------------------------------------------------------//
                            checkFReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot checkReferenceSnapshot) {
                                    if(checkReferenceSnapshot.exists()) {
                                        for(DataSnapshot checkItemSnapshot:checkReferenceSnapshot.getChildren()){

                                            String checkInMain=String.valueOf(mainItemSnapshot.child("check").getValue());
                                            String detailInLostFound=String.valueOf(checkItemSnapshot.child("detail").getValue());
                                            String foundEmailInLostFound=String.valueOf(checkItemSnapshot.child("foundEmail").getValue());

                                            /*if (checkInMain.equals(detailInLostFound) && fEmail.equals(foundEmailInLostFound)){
                                                flagf=1;
                                                LostEmail = String.valueOf(checkItemSnapshot.child("lostEmail").getValue());
                                                break;
                                            }*/

                                            if (checkInMain.equals(detailInLostFound) && fEmail.equals(foundEmailInLostFound) && fEmail.equals(obj1.obj.getEmail())) {
                                                    LostEmail = String.valueOf(checkItemSnapshot.child("lostEmail").getValue());
                                                    RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: "+LostEmail,checkInMain);
                                                    if(fEmail.equals(obj1.obj.getEmail())){
                                                        flagf=1;
                                                       listItems.add(i);
                                                    }
                                                adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                                recyclerView.setAdapter(adapter);
                                                break;
                                            }

                                        }
                                        if(flagf==0){
                                            RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: ",checkInMain);
                                            if(fEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        /*/////////////////////////////////////////////////////////////////////
                                        if(flagf==1 && !(LostEmail.equals(obj1.obj.getEmail()))){
                                            RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: "+LostEmail);
                                            if(fEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        else{
                                            RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: ");
                                            if(fEmail.equals(obj1.obj.getEmail())){
                                                listItems.add(i);
                                            }
                                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                            recyclerView.setAdapter(adapter);
                                        }
                                        ///////////////////////////////////////////////////////////////////////////////////*/
                                    }
                                    else{
                                        RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: ",checkInMain);
                                        if(fEmail.equals(obj1.obj.getEmail())){
                                            listItems.add(i);
                                        }
                                        adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            flagf=0;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(MyAccountActivity.this,TwoButtonsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listItems.clear();
        adapter.notifyDataSetChanged();
    }
}
