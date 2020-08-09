package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telecom.TelecomManager;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class LostAudioActivity extends Activity {

    private Spinner mLostAudioSpinner;
    private Button mLostAudioBackButton;
    private Button mLostAudioSubmitButton;
    private EditText mLostAudioCompanyName;
    private EditText mLostAudioColor;
    public EditText mLostAudioDate;
    private EditText mLostAudioPlace;
    private EditText mLostAudioRoomNo;

    public String LostAudioDate;

    private DatabaseReference lostFound;
    MainActivity obj1 = new MainActivity();
    private String Check;

    FirebaseAuth mAuth;

    DatabaseReference lostAudio;
    String LostAudioType;

    ObjectActivityClass member = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_audio_layout);

        mLostAudioSpinner=(Spinner)findViewById(R.id.LostAudioSpinner);
        mLostAudioBackButton=(Button)findViewById(R.id.LostAudioBackButton);
        mLostAudioSubmitButton=(Button)findViewById(R.id.LostAudioSubmitButton);
        mLostAudioCompanyName=(EditText) findViewById(R.id.LostAudioCompanyName);
        mLostAudioColor=(EditText)findViewById(R.id.LostAudioColor);
        mLostAudioDate=(EditText)findViewById(R.id.LostAudioDate);
        mLostAudioPlace=(EditText)findViewById(R.id.LostAudioPlace);
        mLostAudioRoomNo=(EditText)findViewById(R.id.LostAudioRoomNo);

        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostAudio= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");

        LostAudioSpinner();


        mLostAudioSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LostAudioType.equals("--Select an Item--"))
                    Toast.makeText(LostAudioActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    insertData();
                    Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);

                    queryLostAudio.addListenerForSingleValueEvent(lostAudioListner);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(LostAudioActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }


            ValueEventListener lostAudioListner = new ValueEventListener() {
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
                                notification(lf_Email,LostAudioType, " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),LostAudioType, " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostAudioActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };

        }
        );

        mLostAudioBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostAudioActivity.this,LostPageActivity.class));
                finish();
            }
        });
    }

    public void LostAudioSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostAudioTypes=getResources().getStringArray(R.array.audio_devices);
        List<String> LostAudioTypesList= Arrays.asList(LostAudioTypes);
        //Collections.sort(LostAudioTypesList);
        /////////////////////////////////////////

        ArrayAdapter AudioAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostAudioTypesList);
        AudioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostAudioSpinner.setAdapter(AudioAdapter);
        mLostAudioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostAudioType=adapterView.getItemAtPosition(i).toString();
                if(LostAudioType.equals("Apple Airpods")||LostAudioType.equals("Apple Earphones")){
                    mLostAudioCompanyName.setText("Apple");
                    mLostAudioCompanyName.setFocusable(false);
                    mLostAudioCompanyName.setClickable(false);
                }
                else{
                    mLostAudioCompanyName.setText("");
                    mLostAudioCompanyName.setFocusable(true);
                    mLostAudioCompanyName.setClickable(true);
                }
                Toast.makeText(adapterView.getContext(),LostAudioType,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(LostAudioActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertData(){
        String LostAudioCompanyName=mLostAudioCompanyName.getText().toString().toLowerCase();
        String LostAudioColor=mLostAudioColor.getText().toString().toLowerCase();
        String LostAudioPlace=mLostAudioPlace.getText().toString().toLowerCase();
        String LostAudioRoomNo=mLostAudioRoomNo.getText().toString().toLowerCase();
        Check = LostAudioType+"_"+LostAudioCompanyName + "_" + LostAudioColor + "_" + LostAudioPlace;
        String status = "lost";

        //OnlyEmail eml = new OnlyEmail();

        member.setEmail(obj1.obj.getEmail());
        member.setType(LostAudioType);
        member.setCompanyName(LostAudioCompanyName);
        member.setColour(LostAudioColor);
        member.setDate(LostAudioDate);
        member.setPlace(LostAudioPlace);
        member.setLabNo(LostAudioRoomNo);
        member.setCheck(Check);
        member.setStatus(status);

        lostAudio.push().setValue(member);
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

                LostAudioDate=sdf.format(myCalendar.getTime());
                mLostAudioDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostAudioDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostAudioActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


}
