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

public class LostBookActivity extends Activity {

    private Spinner mLostBookSpinner;
    private Button mLostBookBackButton;
    private Button mLostBookSubmitButton;

    private MainActivity objl = new MainActivity();
    private DatabaseReference lostFound;

    private EditText mLostBookTitle;
    private EditText mLostBookExtraNotes;
    private EditText mLostBookDate;
    private EditText mLostBookPlace;
    private EditText mLostBookRoomNo;

    String LostBookDate;
    String Check;

    DatabaseReference lostBook;
    String LostBookType;
    ObjectActivityClass lostBookMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_book_layout);

        mLostBookSpinner=(Spinner)findViewById(R.id.LostBookSpinner);
        mLostBookBackButton=(Button)findViewById(R.id.LostBookBackButton);
        mLostBookSubmitButton=(Button)findViewById(R.id.LostBookSubmitButton);

        mLostBookTitle=(EditText) findViewById(R.id.LostBookTitle);
        mLostBookExtraNotes=(EditText)findViewById(R.id.LostBookSpecialNotes);
        mLostBookDate=(EditText)findViewById(R.id.LostBookDate);
        mLostBookPlace=(EditText)findViewById(R.id.LostBookPlace);
        mLostBookRoomNo=(EditText)findViewById(R.id.LostBookRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostBook= FirebaseDatabase.getInstance().getReference().child("LostObjectActivity");

        LostBookSpinner();

        mLostBookSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LostBookType.equals("--Select an Item--"))
                    Toast.makeText(LostBookActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getThisInfo();
                    lostBook.push().setValue(lostBookMember);

                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();

                    Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);

                    queryLostAudio.addListenerForSingleValueEvent(lostAudioListner);
                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(LostBookActivity.this,TwoButtonsActivity.class));
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

                            lfobj.setLostEmail(objl.obj.getEmail());
                            lfobj.setFoundEmail(lf_Email);
                            lfobj.setDetail(Check);
                            lfobj.setFoundDate(lf_date);
                            lostFound.push().setValue(lfobj);

                            if(!(objl.obj.getEmail()).equals(lf_Email))
                                notification(lf_Email,LostBookType, " Found By: ", " Found");
                            if((objl.obj.getEmail()).equals(lf_Email))
                                notification(objl.obj.getEmail(),LostBookType, " Lost By: ", " Owner Found");
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LostBookActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };
        });

        mLostBookBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostBookActivity.this,LostPageActivity.class));
                finish();
            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostBookActivity.this,LostPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void LostBookSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostBookTypes=getResources().getStringArray(R.array.book_types);
        List<String> LostBookTypesList= Arrays.asList(LostBookTypes);
        Collections.sort(LostBookTypesList);
        /////////////////////////////////////////

        ArrayAdapter BookAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostBookTypesList);
        BookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostBookSpinner.setAdapter(BookAdapter);
        mLostBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostBookType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostBookType,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(LostBookActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
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

                LostBookDate=sdf.format(myCalendar.getTime());
                mLostBookDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostBookDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostBookActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getThisInfo(){
        String LostBookTitle = mLostBookTitle.getText().toString().toLowerCase();
        String Extra = mLostBookExtraNotes.getText().toString().toLowerCase();
        String LostBookDate = mLostBookDate.getText().toString().toLowerCase();
        String LostBookPlace = mLostBookPlace.getText().toString().toLowerCase();
        String LostBookRoomNo = mLostBookRoomNo.getText().toString().toLowerCase();
        Check = LostBookType+"_"+LostBookTitle+"_"+LostBookPlace;


        lostBookMember.setEmail(objl.obj.getEmail());
        lostBookMember.setType(LostBookType);
        lostBookMember.setCompanyName(LostBookTitle);
        //lostBookMember.setExtra(Extra);
        lostBookMember.setDate(LostBookDate);
        lostBookMember.setPlace(LostBookPlace);
        lostBookMember.setLabNo(LostBookRoomNo);
        lostBookMember.setCheck(Check);
        lostBookMember.setStatus("lost");
    }
}
