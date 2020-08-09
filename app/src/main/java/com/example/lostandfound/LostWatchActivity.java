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

public class LostWatchActivity extends Activity {

    private Spinner mLostWatchSpinner;
    private Button mLostWatchBackButton;
    private Button mLostWatchSubmitButton;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    private EditText mLostWatchCompanyName;
    private EditText mLostWatchColor;
    private EditText mLostWatchDate;
    private EditText mLostWatchPlace;
    private EditText mLostWatchRoomNo;

    DatabaseReference lostWatch;
    String LostWatchType,LostWatchDate;

    ObjectActivityClass lostWatchMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_watch_layout);

        mLostWatchSpinner=(Spinner)findViewById(R.id.LostWatchSpinner);
        mLostWatchBackButton=(Button)findViewById(R.id.LostWatchBackButton);
        mLostWatchSubmitButton=(Button)findViewById(R.id.LostWatchSubmitButton);
        mLostWatchCompanyName=(EditText) findViewById(R.id.LostWatchCompanyName);
        mLostWatchColor=(EditText)findViewById(R.id.LostWatchColor);
        mLostWatchDate=(EditText)findViewById(R.id.LostWatchDate);
        mLostWatchPlace=(EditText)findViewById(R.id.LostWatchPlace);
        mLostWatchRoomNo=(EditText)findViewById(R.id.LostWatchRoomNo);

        DatePicker();

        lostWatch= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");
        LostWatchSpinner();
        // String CompanyName=mLostWatchCompanyName.getText().toString();


        mLostWatchSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LostWatchType.equals("--Select an Item--"))
                    Toast.makeText(LostWatchActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getInfo();
                    lostWatch.push().setValue(lostWatchMember);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();
                    Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);

                    queryLostAudio.addListenerForSingleValueEvent(lostWatchListner);
                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(LostWatchActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }

            ValueEventListener lostWatchListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            WatchActivityClass BAC = snapshot.getValue(WatchActivityClass.class);
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
                                notification(lf_Email,LostWatchType, " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),LostWatchType, " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostWatchActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });



        mLostWatchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostWatchActivity.this,LostPageActivity.class));
                finish();
            }
        });


    }

    private void LostWatchSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostWatchTypes=getResources().getStringArray(R.array.watches);
        List<String> LostWatchTypesList= Arrays.asList(LostWatchTypes);
        Collections.sort(LostWatchTypesList);
        /////////////////////////////////////////

        ArrayAdapter WatchAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostWatchTypesList);
        WatchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostWatchSpinner.setAdapter(WatchAdapter);
        mLostWatchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostWatchType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostWatchType,Toast.LENGTH_SHORT).show();
                mLostWatchSpinner.setPrompt("Select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(LostWatchActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
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

                LostWatchDate=sdf.format(myCalendar.getTime());
                mLostWatchDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostWatchDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostWatchActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){
        String LostWatchCompanyName=mLostWatchCompanyName.getText().toString().toLowerCase();
        String LostWatchColor=mLostWatchColor.getText().toString().toLowerCase();
        String LostWatchDate=mLostWatchDate.getText().toString().toLowerCase();
        String LostWatchPlace=mLostWatchPlace.getText().toString().toLowerCase();
        String LostWatchRoomNo=mLostWatchRoomNo.getText().toString().toLowerCase();
        Check=LostWatchType+"_"+LostWatchCompanyName+"_"+LostWatchColor+"_"+LostWatchPlace;

        lostWatchMember.setEmail(obj1.obj.getEmail());
        lostWatchMember.setType(LostWatchType);
        lostWatchMember.setCompanyName(LostWatchCompanyName);
        lostWatchMember.setColour(LostWatchColor);
        lostWatchMember.setDate(LostWatchDate);
        lostWatchMember.setPlace(LostWatchPlace);
        lostWatchMember.setLabNo(LostWatchRoomNo);
        lostWatchMember.setCheck(Check);
        lostWatchMember.setStatus("lost");
    }
}
