package com.example.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;


public class FoundPageActivity extends Activity {
    private GridLayout mFoundGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_grid_layout);

        mFoundGrid=(GridLayout)findViewById(R.id.FoundGridLayoutID);

        setSingleEvent(mFoundGrid);
    }

    private void setSingleEvent(GridLayout mFoundGrid) {
        final CardView FoundWatchCard=(CardView)mFoundGrid.getChildAt(0);
        final CardView FoundAudioCard=(CardView)mFoundGrid.getChildAt(1);
        final CardView FoundMobileCard=(CardView)mFoundGrid.getChildAt(2);
        final CardView FoundusbCard=(CardView)mFoundGrid.getChildAt(3);
        final CardView FoundBookCard=(CardView)mFoundGrid.getChildAt(4);
        final CardView FoundBagCard=(CardView)mFoundGrid.getChildAt(5);
        final CardView FoundChargerCard=(CardView)mFoundGrid.getChildAt(6);
        final CardView FoundPowerBankCard=(CardView)mFoundGrid.getChildAt(7);
        final CardView FoundOthersCard=(CardView)mFoundGrid.getChildAt(8);

        FoundWatchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Watch", Toast.LENGTH_SHORT).show();
                FoundWatchCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundWatchActivity.class));
                finish();
            }
        });

        FoundAudioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Audio Devices", Toast.LENGTH_SHORT).show();
                FoundAudioCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundAudioActivity.class));
                finish();
            }
        });

        FoundusbCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "USB Devices", Toast.LENGTH_SHORT).show();
                FoundusbCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundusbActivity.class));
                finish();
            }
        });

        FoundBookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Book", Toast.LENGTH_SHORT).show();
                FoundBookCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundBookActivity.class));
                finish();
            }
        });

        FoundBagCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Bag", Toast.LENGTH_SHORT).show();
                FoundBagCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundBagActivity.class));
                finish();
            }
        });

        FoundChargerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Chargers", Toast.LENGTH_SHORT).show();
                FoundChargerCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundChargerActivity.class));
                finish();
            }
        });

        FoundChargerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Chargers", Toast.LENGTH_SHORT).show();
                FoundChargerCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundChargerActivity.class));
                finish();
            }
        });

        FoundPowerBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "PowerBank", Toast.LENGTH_SHORT).show();
                FoundPowerBankCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundPowerBankActivity.class));
                finish();
            }
        });

        FoundMobileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Mobile", Toast.LENGTH_SHORT).show();
                FoundMobileCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundMobileActivity.class));
                finish();
            }
        });

        FoundOthersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoundPageActivity.this, "Others", Toast.LENGTH_SHORT).show();
                FoundOthersCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(FoundPageActivity.this,FoundOthersActivity.class));
                finish();
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundPageActivity.this,TwoButtonsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
