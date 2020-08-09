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

public class LostOthersActivity extends Activity {
    private Button mLostOthersBackButton;
    private Button mLostOthersSubmitButton;

    private EditText mLostOthersItemName;
    private EditText mLostOthersCompanyName;
    private EditText mLostOthersColor;
    private EditText mLostOthersDate;
    private EditText mLostOthersPlace;
    private EditText mLostOthersRoomNo;
    private EditText mLostOthersSpecialNotes;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    String LostOthersDate;

    DatabaseReference lostOthers;

    ObjectActivityClass lostOthersMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_others_layout);

        mLostOthersBackButton=(Button)findViewById(R.id.LostOthersBackButton);
        mLostOthersSubmitButton=(Button)findViewById(R.id.LostOthersSubmitButton);

        mLostOthersCompanyName=(EditText) findViewById(R.id.LostOthersCompanyName);
        mLostOthersItemName=(EditText)findViewById(R.id.LostOthersItemName);
        mLostOthersColor=(EditText)findViewById(R.id.LostOthersColor);
        mLostOthersDate=(EditText)findViewById(R.id.LostOthersDate);
        mLostOthersPlace=(EditText)findViewById(R.id.LostOthersPlace);
        mLostOthersRoomNo=(EditText)findViewById(R.id.LostOthersRoomNo);
        mLostOthersSpecialNotes = (EditText)findViewById(R.id.LostOthersSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostOthers= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");

        mLostOthersSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();
                lostOthers.push().setValue(lostOthersMember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                queryLostAudio.addListenerForSingleValueEvent(lostOthersListner);
                setContentView(R.layout.submitted_layout);
                startActivity(new Intent(LostOthersActivity.this,TwoButtonsActivity.class));
                finish();
            }

            ValueEventListener lostOthersListner = new ValueEventListener() {
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

                            String LostOthersCompanyName=mLostOthersCompanyName.getText().toString();
                            String LostOthersItemName = mLostOthersItemName.getText().toString();
                            String type=LostOthersCompanyName+" "+LostOthersItemName;

                            if(!(obj1.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,type, " Found By: ", " Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),type, " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostOthersActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });

        mLostOthersBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostOthersActivity.this,LostPageActivity.class));
                finish();
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostOthersActivity.this,LostPageActivity.class);
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

                LostOthersDate=sdf.format(myCalendar.getTime());
                mLostOthersDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostOthersDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostOthersActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){

        String LostOthersCompanyName=mLostOthersCompanyName.getText().toString().toLowerCase();
        String LostOthersItemName = mLostOthersItemName.getText().toString().toLowerCase();
        String LostOthersColor=mLostOthersColor.getText().toString().toLowerCase();
        String LostOthersDate=mLostOthersDate.getText().toString().toLowerCase();
        String LostOthersPlace=mLostOthersPlace.getText().toString().toLowerCase();
        String LostOthersRoomNo=mLostOthersRoomNo.getText().toString().toLowerCase();
        String LostOthersSpecialNotes = mLostOthersSpecialNotes.getText().toString().toLowerCase();
        String LostOthersType="others";
        Check=LostOthersType+"_"+LostOthersCompanyName+"_"+LostOthersItemName+"_"+LostOthersColor+"_"+LostOthersPlace;


        lostOthersMember.setEmail(obj1.obj.getEmail());

        lostOthersMember.setItem(LostOthersItemName);
        lostOthersMember.setCompanyName(LostOthersCompanyName);
        lostOthersMember.setColour(LostOthersColor);
        lostOthersMember.setDate(LostOthersDate);
        lostOthersMember.setPlace(LostOthersPlace);
        lostOthersMember.setLabNo(LostOthersRoomNo);
        //lostOthersMember.setSpecialNotes(LostOthersSpecialNotes);
        lostOthersMember.setCheck(Check);
        lostOthersMember.setStatus("lost");
        lostOthersMember.setType("others");
    }
}
