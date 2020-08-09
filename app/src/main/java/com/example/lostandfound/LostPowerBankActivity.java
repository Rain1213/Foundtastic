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

public class LostPowerBankActivity extends Activity {
    private Button mLostPowerBankBackButton;
    private Button mLostPowerBankSubmitButton;

    private EditText mLostPowerBankCapacity;
    private EditText mLostPowerBankCompanyName;
    private EditText mLostPowerBankColor;
    private EditText mLostPowerBankDate;
    private EditText mLostPowerBankPlace;
    private EditText mLostPowerBankRoomNo;
    private EditText mLostPowerBankSpecialNotes;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    String LostPowerBankDate;

    DatabaseReference lostPowerBank;

    ObjectActivityClass lostPowerBankMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_powerbank_layout);

        mLostPowerBankBackButton=(Button)findViewById(R.id.LostPowerBankBackButton);
        mLostPowerBankSubmitButton=(Button)findViewById(R.id.LostPowerBankSubmitButton);

        mLostPowerBankCompanyName=(EditText) findViewById(R.id.LostPowerBankCompanyName);
        mLostPowerBankCapacity=(EditText)findViewById(R.id.LostPowerBankCapacity);
        mLostPowerBankColor=(EditText)findViewById(R.id.LostPowerBankColor);
        mLostPowerBankDate=(EditText)findViewById(R.id.LostPowerBankDate);
        mLostPowerBankPlace=(EditText)findViewById(R.id.LostPowerBankPlace);
        mLostPowerBankRoomNo=(EditText)findViewById(R.id.LostPowerBankRoomNo);
        mLostPowerBankSpecialNotes = (EditText)findViewById(R.id.LostPowerBankSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostPowerBank= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");

        mLostPowerBankSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

                lostPowerBank.push().setValue(lostPowerBankMember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query queryLost = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                queryLost.addListenerForSingleValueEvent(lostPowerBankListner);

                setContentView(R.layout.submitted_layout);
                startActivity(new Intent(LostPowerBankActivity.this,TwoButtonsActivity.class));
                finish();
            }

            ValueEventListener lostPowerBankListner = new ValueEventListener() {
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
                                notification(lf_Email,"PowerBank", " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),"PowerBank", " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostPowerBankActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });

        mLostPowerBankBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostPowerBankActivity.this,LostPageActivity.class));
                finish();
            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostPowerBankActivity.this,LostPageActivity.class);
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

                LostPowerBankDate=sdf.format(myCalendar.getTime());
                mLostPowerBankDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostPowerBankDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostPowerBankActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getInfo(){

        String LostPowerBankCompanyName=mLostPowerBankCompanyName.getText().toString().toLowerCase();
        String LostPowerBankCapacity = mLostPowerBankCapacity.getText().toString().toLowerCase();
        String LostPowerBankColor=mLostPowerBankColor.getText().toString().toLowerCase();
        String LostPowerBankDate=mLostPowerBankDate.getText().toString().toLowerCase();
        String LostPowerBankPlace=mLostPowerBankPlace.getText().toString().toLowerCase();
        String LostPowerBankRoomNo=mLostPowerBankRoomNo.getText().toString().toLowerCase();
        String LostPowerBankSpecialNotes = mLostPowerBankSpecialNotes.getText().toString().toLowerCase();
        String LostPowerBankType="powerbank";
        Check=LostPowerBankType+"_"+LostPowerBankCompanyName+"_"+LostPowerBankCapacity+"_"+LostPowerBankColor+"_"+LostPowerBankPlace;


        lostPowerBankMember.setType(LostPowerBankType);
        lostPowerBankMember.setEmail(obj1.obj.getEmail());
        lostPowerBankMember.setDataCapacity(LostPowerBankCapacity);
        lostPowerBankMember.setCompanyName(LostPowerBankCompanyName);
        lostPowerBankMember.setColour(LostPowerBankColor);
        lostPowerBankMember.setDate(LostPowerBankDate);
        lostPowerBankMember.setPlace(LostPowerBankPlace);
        lostPowerBankMember.setLabNo(LostPowerBankRoomNo);
        //lostPowerBankMember.setSpecialNotes(LostPowerBankSpecialNotes);
        lostPowerBankMember.setCheck(Check);
        lostPowerBankMember.setStatus("lost");
    }
}
