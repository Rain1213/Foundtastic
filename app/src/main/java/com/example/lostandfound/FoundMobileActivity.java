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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoundMobileActivity extends Activity {
    private Button mFoundMobileBackButton;
    private Button mFoundMobileSubmitButton;

    private EditText mFoundMobileCompanyName;
    private EditText mFoundMobileModelName;
    private EditText mFoundMobileColor;
    private EditText mFoundMobileDate;
    private EditText mFoundMobilePlace;
    private EditText mFoundMobileRoomNo;
    private EditText mFoundMobileSpecialNotes;

    String FoundMobileDate;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    String Check;

    DatabaseReference foundMobile;

    ObjectActivityClass foundMobileMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_mobile_layout);

        mFoundMobileBackButton=(Button)findViewById(R.id.FoundMobileBackButton);
        mFoundMobileSubmitButton=(Button)findViewById(R.id.FoundMobileSubmitButton);

        mFoundMobileCompanyName=(EditText) findViewById(R.id.FoundMobileCompanyName);
        mFoundMobileModelName=(EditText)findViewById(R.id.FoundMobileModel);
        mFoundMobileColor=(EditText)findViewById(R.id.FoundMobileColor);
        mFoundMobileDate=(EditText)findViewById(R.id.FoundMobileDate);
        mFoundMobilePlace=(EditText)findViewById(R.id.FoundMobilePlace);
        mFoundMobileRoomNo=(EditText)findViewById(R.id.FoundMobileRoomNo);
        mFoundMobileSpecialNotes = (EditText)findViewById(R.id.FoundMobileSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundMobile= FirebaseDatabase.getInstance().getReference().child("FoundObjectActivity");

        mFoundMobileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundMobileActivity.this,FoundPageActivity.class));
                finish();
            }
        });

        mFoundMobileSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();
                foundMobile.push().setValue(foundMobileMember);

                Query query3 = FirebaseDatabase.getInstance().getReference("LostObjectActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                query3.addListenerForSingleValueEvent(foundMobileListner);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                setContentView(R.layout.submitted_layout);
                startActivity(new Intent(FoundMobileActivity.this,TwoButtonsActivity.class));
                finish();
            }

            ValueEventListener foundMobileListner = new ValueEventListener() {
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

                            if(!(obj1.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,"Mobile", " Lost By: ", " Owner Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),"Mobile", " Found By: ", " Found");
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FoundMobileActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundMobileActivity.this,FoundPageActivity.class);
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

                FoundMobileDate=sdf.format(myCalendar.getTime());
                mFoundMobileDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundMobileDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundMobileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getInfo(){

        String FoundMobileCompanyName=mFoundMobileCompanyName.getText().toString().toLowerCase();
        String FoundMobileModelName = mFoundMobileModelName.getText().toString().toLowerCase();
        String FoundMobileColor=mFoundMobileColor.getText().toString().toLowerCase();
        String FoundMobileDate=mFoundMobileDate.getText().toString().toLowerCase();
        String FoundMobilePlace=mFoundMobilePlace.getText().toString().toLowerCase();
        String FoundMobileRoomNo=mFoundMobileRoomNo.getText().toString().toLowerCase();
        String FoundMobileSpecialNotes = mFoundMobileSpecialNotes.getText().toString().toLowerCase();
        String FoundMobileType="mobile";
        Check=FoundMobileType+"_"+FoundMobileCompanyName+"_"+FoundMobileModelName+"_"+FoundMobilePlace;

        foundMobileMember.setEmail(obj1.obj.getEmail());
        foundMobileMember.setType(FoundMobileType);
        foundMobileMember.setCompanyName(FoundMobileCompanyName);
        foundMobileMember.setModelName(FoundMobileModelName);
        foundMobileMember.setColour(FoundMobileColor);
        foundMobileMember.setDate(FoundMobileDate);
        foundMobileMember.setPlace(FoundMobilePlace);
        foundMobileMember.setLabNo(FoundMobileRoomNo);
        //foundMobileMember.setSpecialNotes(FoundMobileSpecialNotes);
        foundMobileMember.setCheck(Check);
        foundMobileMember.setStatus("found");
    }
}
