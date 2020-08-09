package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FoundusbActivity extends Activity {

    private Spinner mFoundusbSpinner;
    private Button mFoundusbBackButton;
    private Button mFoundusbSubmitButton;

    private EditText mFoundusbCompanyName;
    private EditText mFoundusbColor;
    private EditText mFoundusbDataCapacity;
    private EditText mFoundusbDate;
    private EditText mFoundusbPlace;
    private EditText mFoundusbRoomNo;
    DatabaseReference foundusb;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    String FoundusbType,FoundusbDate;

    ObjectActivityClass foundusbmember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_usb_layout);

        mFoundusbSpinner=(Spinner)findViewById(R.id.FoundusbSpinner);
        mFoundusbBackButton=(Button)findViewById(R.id.FoundusbBackButton);
        mFoundusbSubmitButton=(Button)findViewById(R.id.FoundusbSubmitButton);


        mFoundusbCompanyName=(EditText) findViewById(R.id.FoundusbCompanyName);
        mFoundusbColor=(EditText)findViewById(R.id.FoundusbColor);
        mFoundusbDataCapacity = (EditText)findViewById(R.id.FoundusbDataCapacity);
        mFoundusbDate=(EditText)findViewById(R.id.FoundusbDate);
        mFoundusbPlace=(EditText)findViewById(R.id.FoundusbPlace);
        mFoundusbRoomNo=(EditText)findViewById(R.id.FoundusbRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundusb= FirebaseDatabase.getInstance().getReference().child(" FoundObjectActivity");

        FoundusbSpinner();

        mFoundusbBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundusbActivity.this,FoundPageActivity.class));
                finish();
            }
        });

        mFoundusbSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FoundusbType.equals("--Select an Item--"))
                    Toast.makeText(FoundusbActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getInfo();

                    foundusb.push().setValue(foundusbmember);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();

                    Query query3 = FirebaseDatabase.getInstance().getReference("LostObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);
                    query3.addListenerForSingleValueEvent(foundusbListner);
                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(FoundusbActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }
            ValueEventListener foundusbListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AudioActivityClass BAC = snapshot.getValue(AudioActivityClass.class);
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
                                notification(lf_Email,FoundusbType, " Lost By: ", " Owner Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),FoundusbType, " Found By: ", " Found");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FoundusbActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };

        });

    }

    private void FoundusbSpinner() {
        //******Sort Items Alphabetically******//
        String[] FoundusbTypes=getResources().getStringArray(R.array.usb_devices);
        List<String> FoundusbTypesList= Arrays.asList(FoundusbTypes);
        Collections.sort(FoundusbTypesList);
        /////////////////////////////////////////

        ArrayAdapter usbAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,FoundusbTypesList);
        usbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFoundusbSpinner.setAdapter(usbAdapter);
        mFoundusbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                FoundusbType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),FoundusbType,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(FoundusbActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
            }
        });
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

                FoundusbDate=sdf.format(myCalendar.getTime());
                mFoundusbDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundusbDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundusbActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void getInfo(){

        String FoundusbCompanyName=mFoundusbCompanyName.getText().toString().toLowerCase();
        String FoundusbColor=mFoundusbColor.getText().toString().toLowerCase();
        String FoundusbCapacity = mFoundusbDataCapacity.getText().toString().toLowerCase();
        String FoundusbDate=mFoundusbDate.getText().toString().toLowerCase();
        String FoundusbPlace=mFoundusbPlace.getText().toString().toLowerCase();
        String FoundusbRoomNo=mFoundusbRoomNo.getText().toString().toLowerCase();
        String FoundusbType="usb";
        Check=FoundusbType+"_"+FoundusbCompanyName+"_"+FoundusbColor+"_"+FoundusbCapacity+"_"+FoundusbPlace;

        foundusbmember.setType(FoundusbType);
        foundusbmember.setEmail(obj1.obj.getEmail());
        foundusbmember.setUsbType(FoundusbType);
        foundusbmember.setCompanyName(FoundusbCompanyName);
        foundusbmember.setColour(FoundusbColor);
        foundusbmember.setDataCapacity(FoundusbCapacity);
        foundusbmember.setDate(FoundusbDate);
        foundusbmember.setPlace(FoundusbPlace);
        foundusbmember.setLabNo(FoundusbRoomNo);
        foundusbmember.setCheck(Check);
        foundusbmember.setStatus("found");
    }
}
