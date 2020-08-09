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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoundOthersActivity extends Activity {
    private Button mFoundOthersBackButton;
    private Button mFoundOthersSubmitButton;

    private EditText mFoundOthersCompanyName;
    private EditText mFoundOthersItemName;
    private EditText mFoundOthersColor;
    private EditText mFoundOthersDate;
    private EditText mFoundOthersPlace;
    private EditText mFoundOthersRoomNo;
    private EditText mFoundOthersSpecialNotes;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    String FoundOthersDate;

    DatabaseReference foundOthers;

    ObjectActivityClass foundOthersMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_others_layout);

        mFoundOthersBackButton=(Button)findViewById(R.id.FoundOthersBackButton);
        mFoundOthersSubmitButton=(Button)findViewById(R.id.FoundOthersSubmitButton);

        mFoundOthersCompanyName=(EditText) findViewById(R.id.FoundOthersCompanyName);
        mFoundOthersItemName=(EditText)findViewById(R.id.FoundOthersItemName);
        mFoundOthersColor=(EditText)findViewById(R.id.FoundOthersColor);
        mFoundOthersDate=(EditText)findViewById(R.id.FoundOthersDate);
        mFoundOthersPlace=(EditText)findViewById(R.id.FoundOthersPlace);
        mFoundOthersRoomNo=(EditText)findViewById(R.id.FoundOthersRoomNo);
        mFoundOthersSpecialNotes = (EditText)findViewById(R.id.FoundOthersSpecialNotes);

        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundOthers= FirebaseDatabase.getInstance().getReference().child("FoundObjectActivity");

        mFoundOthersSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();
                foundOthers.push().setValue(foundOthersMember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query query3 = FirebaseDatabase.getInstance().getReference("LostObjectActivity")
                        .orderByChild("check")
                        .equalTo(Check);
                query3.addListenerForSingleValueEvent(foundOthersListner);
                setContentView(R.layout.submitted_layout);
                startActivity(new Intent(FoundOthersActivity.this,TwoButtonsActivity.class));
                finish();
            }
            ValueEventListener foundOthersListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AudioActivityClass BAC = snapshot.getValue(AudioActivityClass.class);
                            String lf_Email = BAC.getEmail();
                            String lf_date = BAC.getDate();

                            lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                            LostFound lfobj = new LostFound();
                            lfobj.setFoundEmail(obj1.obj.getEmail());
                            lfobj.setLostEmail(lf_Email);
                            lfobj.setDetail(Check);
                            lfobj.setFoundDate(lf_date);
                            lostFound.push().setValue(lfobj);

                            String FoundOthersCompanyName=mFoundOthersCompanyName.getText().toString();
                            String FoundOthersItemName = mFoundOthersItemName.getText().toString();
                            String type=FoundOthersCompanyName+" "+FoundOthersItemName;

                            if(!(obj1.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,type, " Lost By: ", " Owner Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),type, " Found By: ", " Found");

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FoundOthersActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });

        mFoundOthersBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundOthersActivity.this,FoundPageActivity.class));
                finish();
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundOthersActivity.this,FoundPageActivity.class);
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

                FoundOthersDate=sdf.format(myCalendar.getTime());
                mFoundOthersDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundOthersDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundOthersActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getInfo(){

        String FoundOthersCompanyName=mFoundOthersCompanyName.getText().toString();
        String FoundOthersItemName = mFoundOthersItemName.getText().toString();
        String FoundOthersColor=mFoundOthersColor.getText().toString();
        String FoundOthersDate=mFoundOthersDate.getText().toString();
        String FoundOthersPlace=mFoundOthersPlace.getText().toString();
        String FoundOthersRoomNo=mFoundOthersRoomNo.getText().toString();
        String FoundOthersSpecialNotes = mFoundOthersSpecialNotes.getText().toString();
        String FoundOthersType="others";
        Check=FoundOthersType+"_"+FoundOthersCompanyName+"_"+FoundOthersItemName+"_"+FoundOthersColor+"_"+FoundOthersPlace;


        foundOthersMember.setEmail(obj1.obj.getEmail());
        foundOthersMember.setItem(FoundOthersItemName);
        foundOthersMember.setCompanyName(FoundOthersCompanyName);
        foundOthersMember.setColour(FoundOthersColor);
        foundOthersMember.setDate(FoundOthersDate);
        foundOthersMember.setPlace(FoundOthersPlace);
        foundOthersMember.setLabNo(FoundOthersRoomNo);
        //foundOthersMember.setSpecialNotes(FoundOthersSpecialNotes);
        foundOthersMember.setCheck(Check);
        foundOthersMember.setStatus("found");
        foundOthersMember.setType("others");
    }
}
