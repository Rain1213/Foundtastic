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

public class LostusbActivity extends Activity {

    private Spinner mLostusbSpinner;
    private Button mLostusbBackButton;
    private Button mLostusbSubmitButton;

    private EditText mLostusbCompanyName;
    private EditText mLostusbColor;
    private EditText mLostusbDataCapacity;
    private EditText mLostusbDate;
    private EditText mLostusbPlace;
    private EditText mLostusbRoomNo;
    DatabaseReference lostusb;
    String LostusbType,LostusbDate;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    ObjectActivityClass lostusbmember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_usb_layout);

        mLostusbSpinner=(Spinner)findViewById(R.id.LostusbSpinner);
        mLostusbBackButton=(Button)findViewById(R.id.LostusbBackButton);
        mLostusbSubmitButton=(Button)findViewById(R.id.LostusbSubmitButton);

        mLostusbBackButton=(Button)findViewById(R.id.LostusbBackButton);
        mLostusbSubmitButton=(Button)findViewById(R.id.LostusbSubmitButton);
        mLostusbCompanyName=(EditText) findViewById(R.id.LostusbCompanyName);
        mLostusbColor=(EditText)findViewById(R.id.LostusbColor);
        mLostusbDataCapacity = (EditText)findViewById(R.id.LostusbDataCapacity);
        mLostusbDate=(EditText)findViewById(R.id.LostusbDate);
        mLostusbPlace=(EditText)findViewById(R.id.LostusbPlace);
        mLostusbRoomNo=(EditText)findViewById(R.id.LostusbRoomNo);

        DatePicker();

        lostusb= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");

        LostusbSpinner();

        mLostusbBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostusbActivity.this,LostPageActivity.class));
                finish();
            }
        });

        mLostusbSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LostusbType.equals("--Select an Item--"))
                    Toast.makeText(LostusbActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getInfo();
                    lostusb.push().setValue(lostusbmember);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();

                    Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);

                    queryLostAudio.addListenerForSingleValueEvent(lostusbListner);

                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(LostusbActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }

            ValueEventListener lostusbListner = new ValueEventListener() {
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
                                notification(lf_Email,LostusbType, " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),LostusbType, " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostusbActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });

    }

    private void LostusbSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostusbTypes=getResources().getStringArray(R.array.usb_devices);
        List<String> LostusbTypesList= Arrays.asList(LostusbTypes);
        Collections.sort(LostusbTypesList);
        /////////////////////////////////////////

        ArrayAdapter usbAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostusbTypesList);
        usbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostusbSpinner.setAdapter(usbAdapter);
        mLostusbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostusbType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostusbType,Toast.LENGTH_SHORT).show();
                mLostusbSpinner.setPrompt("Select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(LostusbActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
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

                LostusbDate=sdf.format(myCalendar.getTime());
                mLostusbDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostusbDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostusbActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){

        String LostusbCompanyName=mLostusbCompanyName.getText().toString().toLowerCase();
        String LostusbColor=mLostusbColor.getText().toString().toLowerCase();
        String LostusbCapacity = mLostusbDataCapacity.getText().toString().toLowerCase();
        String LostusbDate=mLostusbDate.getText().toString().toLowerCase();
        String LostusbPlace=mLostusbPlace.getText().toString().toLowerCase();
        String LostusbRoomNo=mLostusbRoomNo.getText().toString().toLowerCase();
        String LostusbType="usb";
        Check=LostusbType+"_"+LostusbCompanyName+"_"+LostusbColor+"_"+LostusbCapacity+"_"+LostusbPlace;

        lostusbmember.setType(LostusbType);
        lostusbmember.setEmail(obj1.obj.getEmail());
        lostusbmember.setUsbType(LostusbType);
        lostusbmember.setCompanyName(LostusbCompanyName);
        lostusbmember.setColour(LostusbColor);
        lostusbmember.setDataCapacity(LostusbCapacity);
        lostusbmember.setDate(LostusbDate);
        lostusbmember.setPlace(LostusbPlace);
        lostusbmember.setLabNo(LostusbRoomNo);
        lostusbmember.setCheck(Check);
        lostusbmember.setStatus("lost");
    }
}
