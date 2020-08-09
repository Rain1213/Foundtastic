package com.example.lostandfound;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import Adapter.FSliderAdapter;
import Adapter.LSliderAdapter;

public class LostPageActivity extends AppCompatActivity{

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private LSliderAdapter LsliderAdapter;
    private FSliderAdapter FsliderAdapter;
    private Button mPrevBtn;
    private Button mNextBtn;

    private int mCurrentPage;
    private TextView[] mDots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_view_layout);

        mSlideViewPager=findViewById(R.id.slideViewPager);
        mDotsLayout=findViewById(R.id.dotsLayout);

        mPrevBtn=findViewById(R.id.prevButton);
        mNextBtn=findViewById(R.id.nextButton);

        if(getIntent().getStringExtra("from").equals("LostButton")){
            LsliderAdapter=new LSliderAdapter(this);
            mSlideViewPager.setAdapter(LsliderAdapter);
        }

        else if(getIntent().getStringExtra("from").equals("FoundButton")){
            FsliderAdapter=new FSliderAdapter(this);
            mSlideViewPager.setAdapter(FsliderAdapter);
        }

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage+1);
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots=new TextView[9];
        mDotsLayout.removeAllViews();

        for(int i=0;i<mDots.length;i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.the_grey_color));

            mDotsLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(Color.CYAN);
        }
    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            addDotsIndicator(position);

            mCurrentPage=position;
            if(position==0){
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(false);
                mPrevBtn.setVisibility(View.INVISIBLE);

                mNextBtn.setText("NEXT");
                mPrevBtn.setText("");
            }
            else if(position==mDots.length-1){
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("FINISH");
                mPrevBtn.setText("BACK");
            }
            else{
                mNextBtn.setEnabled(true);
                mPrevBtn.setEnabled(true);
                mPrevBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("NEXT");
                mPrevBtn.setText("BACK");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(LostPageActivity.this,TwoButtonsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
