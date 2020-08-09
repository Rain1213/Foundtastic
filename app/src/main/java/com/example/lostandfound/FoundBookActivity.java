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

public class FoundBookActivity extends Activity {

    private Spinner mFoundBookSpinner;
    private Button mFoundBookBackButton;
    private Button mFoundBookSubmitButton;

    private EditText mFoundBookTitle;
    private EditText mFoundBookExtraNotes;
    private EditText mFoundBookDate;
    private EditText mFoundBookPlace;
    private EditText mFoundBookRoomNo;
    String FoundBookDate;

    private String Check;
    private DatabaseReference lostFound;
    MainActivity obj1 = new MainActivity();

    DatabaseReference FoundBook;
    String FoundBookType;

    ObjectActivityClass foundBookMember = new ObjectActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_book_layout);

        mFoundBookSpinner=(Spinner)findViewById(R.id.FoundBookSpinner);
        mFoundBookBackButton=(Button)findViewById(R.id.FoundBookBackButton);
        mFoundBookSubmitButton=(Button)findViewById(R.id.FoundBookSubmitButton);

        mFoundBookTitle=(EditText) findViewById(R.id.FoundBookTitle);
        mFoundBookExtraNotes=(EditText)findViewById(R.id.FoundBookSpecialNotes);
        mFoundBookDate=(EditText)findViewById(R.id.FoundBookDate);
        mFoundBookPlace=(EditText)findViewById(R.id.FoundBookPlace);
        mFoundBookRoomNo=(EditText)findViewById(R.id.FoundBookRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        FoundBook= FirebaseDatabase.getInstance().getReference().child("FoundObjectActivity");

        FoundBookSpinner();

        mFoundBookSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FoundBookType.equals("--Select an Item--"))
                    Toast.makeText(FoundBookActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
                else {
                    getInfo();
                    FoundBook.push().setValue(foundBookMember);
                    Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_SHORT).show();

                    Query query3 = FirebaseDatabase.getInstance().getReference("LostObjectActivity")
                            .orderByChild("check")
                            .equalTo(Check);

                    setContentView(R.layout.submitted_layout);
                    startActivity(new Intent(FoundBookActivity.this,TwoButtonsActivity.class));
                    finish();
                }
            }
            ValueEventListener foundAudioListner = new ValueEventListener() {
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
                                notification(lf_Email,FoundBookType, " Lost By: ", " Owner Found");
                            if((obj1.obj.getEmail()).equals(lf_Email))
                                notification(obj1.obj.getEmail(),FoundBookType, " Found By: ", " Found");
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(FoundBookActivity.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
                }
            };

        });

        mFoundBookBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundBookActivity.this,FoundPageActivity.class));
                finish();
            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundBookActivity.this,FoundPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void FoundBookSpinner() {
        //******Sort Items Alphabetically******//
        String[] FoundBookTypes=getResources().getStringArray(R.array.book_types);
        List<String> FoundBookTypesList= Arrays.asList(FoundBookTypes);
        Collections.sort(FoundBookTypesList);
        /////////////////////////////////////////

        ArrayAdapter BookAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,FoundBookTypesList);
        BookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFoundBookSpinner.setAdapter(BookAdapter);
        mFoundBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                FoundBookType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),FoundBookType,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(FoundBookActivity.this, "Please Select an Item", Toast.LENGTH_SHORT).show();
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

                FoundBookDate=sdf.format(myCalendar.getTime());
                mFoundBookDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundBookDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundBookActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){

        String FoundBookTitle = mFoundBookTitle.getText().toString().toLowerCase();
        String Extra = mFoundBookExtraNotes.getText().toString().toLowerCase();
        String FoundBookDate = mFoundBookDate.getText().toString().toLowerCase();
        String FoundBookPlace = mFoundBookPlace.getText().toString().toLowerCase();
        String FoundBookRoomNo = mFoundBookRoomNo.getText().toString().toLowerCase();
        Check = FoundBookType+"_"+FoundBookTitle+"_"+FoundBookPlace;



        foundBookMember.setEmail(obj1.obj.getEmail());
        foundBookMember.setType(FoundBookType);
        foundBookMember.setCompanyName(FoundBookTitle);
        //foundBookMember.setExtra(Extra);
        foundBookMember.setDate(FoundBookDate);
        foundBookMember.setPlace(FoundBookPlace);
        foundBookMember.setLabNo(FoundBookRoomNo);
        foundBookMember.setCheck(Check);
        foundBookMember.setStatus("found");
    }

}
