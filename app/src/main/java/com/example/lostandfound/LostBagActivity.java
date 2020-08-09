package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LostBagActivity extends Activity {
    private Button mLostBagBackButton;
    private Button mLostBagSubmitButton;
    private EditText mLostBagCompanyName;
    private EditText mLostBagColor;
    private EditText mLostBagDate;
    private EditText mLostBagPlace;
    private EditText mLostBagRoomNo;
    private EditText mLostBagSpecialNotes;

    String LostBagDate;

    private String Check;
    private DatabaseReference lostFound;
    MainActivity obj1 = new MainActivity();
    //ValueEventListener vel;


    DatabaseReference lostBag;
    ObjectActivityClass lostBagMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_bag_layout);

        mLostBagSubmitButton = (Button) findViewById(R.id.LostBagSubmitButton);
        mLostBagBackButton = (Button) findViewById(R.id.LostBagBackButton);
        mLostBagCompanyName = (EditText) findViewById(R.id.LostBagCompanyName);
        mLostBagColor = (EditText) findViewById(R.id.LostBagColor);
        mLostBagDate = (EditText) findViewById(R.id.LostBagDate);
        mLostBagPlace = (EditText) findViewById(R.id.LostBagPlace);
        mLostBagRoomNo = (EditText) findViewById(R.id.LostBagRoomNo);
        mLostBagSpecialNotes = (EditText) findViewById(R.id.LostBagSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostBag = FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");


        mLostBagBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostBagActivity.this, LostPageActivity.class));
                finish();
            }
        });

        mLostBagSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String LostBagCompanyName = mLostBagCompanyName.getText().toString().toLowerCase();
                String LostBagColor = mLostBagColor.getText().toString().toLowerCase();
                String LostBagDate = mLostBagDate.getText().toString().toLowerCase();
                String LostBagPlace = mLostBagPlace.getText().toString().toLowerCase();
                String LostBagRoomNo = mLostBagRoomNo.getText().toString().toLowerCase();
                String LostBagExtra = mLostBagSpecialNotes.getText().toString().toLowerCase();
                String LostBagType ="bag";

                Check = LostBagType+"_"+LostBagCompanyName + "_" + LostBagColor + "_" + LostBagPlace;


                lostBagMember.setEmail(obj1.obj.getEmail());
                lostBagMember.setType(LostBagType);
                lostBagMember.setCompanyName(LostBagCompanyName);
                lostBagMember.setColour(LostBagColor);
                lostBagMember.setDate(LostBagDate);
                lostBagMember.setPlace(LostBagPlace);
                lostBagMember.setLabNo(LostBagRoomNo);
                //lostBagMember.setExtra(LostBagExtra);
                lostBagMember.setCheck(Check);
                lostBagMember.setStatus("lost");
                lostBag.push().setValue(lostBagMember);

                /////DatabaseReference familyListReference = FirebaseDatabase.getInstance().getReference().child("FoundBagActivity");

                Query query3 = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                        .orderByChild("check")
                        .equalTo(Check) ;

                query3.addListenerForSingleValueEvent(al);

                Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.submitted_layout);
                startActivity(new Intent(LostBagActivity.this,TwoButtonsActivity.class));
                finish();
            }

            ValueEventListener al = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            BagActivityClass BAC = snapshot.getValue(BagActivityClass.class);
                            String lf_Email = BAC.getEmail();
                            String lf_date = BAC.getDate();

                            lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                            LostFound lfobj = new LostFound();

                            lfobj.setLostEmail(obj1.obj.getEmail());
                            lfobj.setFoundEmail(lf_Email);
                            lfobj.setDetail(Check);
                            lfobj.setFoundDate(lf_date);

                            lostFound.push().setValue(lfobj);

                            if(!(obj1.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,"Bag", " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),"Bag", " Lost By: ", " Owner Found");
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostBagActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }

            };

        }

        );
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostBagActivity.this,LostPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void notification(String lostEmail,String type,String inMessage,String inHeading){
        String message=type+inMessage+lostEmail;

        NotificationCompat.Builder builder=new NotificationCompat.Builder(
                getApplicationContext()
        )
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(type+inHeading)
                .setContentText(message)
                .setAutoCancel(true);

        Intent intent=new Intent(getApplicationContext(),NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);

        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),
                0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        notificationManager.notify(0,builder.build());
    }

    public void DatePicker(){
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                LostBagDate=sdf.format(myCalendar.getTime());
                mLostBagDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostBagDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostBagActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


}
