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

public class FoundWatchActivity extends Activity {

    private Spinner mFoundWatchSpinner;
    private Button mFoundWatchBackButton;
    private Button mFoundWatchSubmitButton;


    private String Check;
    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();

    private EditText mFoundWatchCompanyName;
    private EditText mFoundWatchColor;
    private EditText mFoundWatchDate;
    private EditText mFoundWatchPlace;
    private EditText mFoundWatchRoomNo;
    DatabaseReference foundWatch;
    String FoundWatchType,FoundWatchDate;

    ObjectActivityClass FoundWatchMember = new ObjectActivityClass();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_watch_layout);

        mFoundWatchSpinner=(Spinner)findViewById(R.id.FoundWatchSpinner);
        mFoundWatchBackButton=(Button)findViewById(R.id.FoundWatchBackButton);
        mFoundWatchSubmitButton=(Button)findViewById(R.id.FoundWatchSubmitButton);
        mFoundWatchCompanyName=(EditText) findViewById(R.id.FoundWatchCompanyName);

        mFoundWatchColor=(EditText)findViewById(R.id.FoundWatchColor);
        mFoundWatchDate=(EditText)findViewById(R.id.FoundWatchDate);
        mFoundWatchPlace=(EditText)findViewById(R.id.FoundWatchPlace);
        mFoundWatchRoomNo=(EditText)findViewById(R.id.FoundWatchRoomNo);

        DatePicker();
        foundWatch= FirebaseDatabase.getInstance().getReference().child("FoundObjectActivity");

        //String CompanyName=mFoundWatchCompanyName.getText().toString();
        FoundWatchSpinner();

        mFoundWatchSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FoundWatchType.equals("--Select an Item"))
                    Toast.makeText(FoundWatchActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getInfo();
                    foundWatch.push().setValue(FoundWatchMember);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();
                    Query queryLostAudio = FirebaseDatabase.getInstance().getReference("LostObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);


                    queryLostAudio.addListenerForSingleValueEvent(foundWatchListner);
                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(FoundWatchActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }

            ValueEventListener foundWatchListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            WatchActivityClass BAC = snapshot.getValue(WatchActivityClass.class);
                            String lf_Email = BAC.getEmail();
                            String lf_date = BAC.getDate();
                            lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                            LostFound lfobj = new LostFound();

                            lfobj.setLostEmail(lf_Email);
                            lfobj.setFoundEmail(obj1.obj.getEmail());
                            lfobj.setDetail(Check);
                            lfobj.setFoundDate(lf_date);
                            lostFound.push().setValue(lfobj);

                            if(!(obj1.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,FoundWatchType, " Lost By: ", " Owner Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),FoundWatchType, " Found By: ", " Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FoundWatchActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });



        mFoundWatchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundWatchActivity.this,FoundPageActivity.class));
                finish();
            }
        });


    }

    private void FoundWatchSpinner() {
        //******Sort Items Alphabetically******//
        String[] FoundWatchTypes=getResources().getStringArray(R.array.watches);
        List<String> FoundWatchTypesList= Arrays.asList(FoundWatchTypes);
        Collections.sort(FoundWatchTypesList);
        /////////////////////////////////////////

        ArrayAdapter WatchAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,FoundWatchTypesList);
        WatchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFoundWatchSpinner.setAdapter(WatchAdapter);
        mFoundWatchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                FoundWatchType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),FoundWatchType,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(FoundWatchActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
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

                FoundWatchDate=sdf.format(myCalendar.getTime());
                mFoundWatchDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundWatchDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundWatchActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){
        String FoundWatchCompanyName=mFoundWatchCompanyName.getText().toString().toLowerCase();
        String FoundWatchColor=mFoundWatchColor.getText().toString().toLowerCase();
        String FoundWatchDate=mFoundWatchDate.getText().toString().toLowerCase();
        String FoundWatchPlace=mFoundWatchPlace.getText().toString().toLowerCase();
        String FoundWatchRoomNo=mFoundWatchRoomNo.getText().toString().toLowerCase();
        Check=FoundWatchType+"_"+FoundWatchCompanyName+"_"+FoundWatchColor+"_"+FoundWatchPlace;



        FoundWatchMember.setCheck(Check);
        FoundWatchMember.setEmail(obj1.obj.getEmail());
        FoundWatchMember.setType(FoundWatchType);
        FoundWatchMember.setCompanyName(FoundWatchCompanyName);
        FoundWatchMember.setColour(FoundWatchColor);
        FoundWatchMember.setDate(FoundWatchDate);
        FoundWatchMember.setPlace(FoundWatchPlace);
        FoundWatchMember.setLabNo(FoundWatchRoomNo);
        FoundWatchMember.setStatus("found");
    }

}
