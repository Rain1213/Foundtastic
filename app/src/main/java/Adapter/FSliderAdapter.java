package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.lostandfound.FoundAudioActivity;
import com.example.lostandfound.FoundBagActivity;
import com.example.lostandfound.FoundBookActivity;
import com.example.lostandfound.FoundChargerActivity;
import com.example.lostandfound.FoundMobileActivity;
import com.example.lostandfound.FoundOthersActivity;
import com.example.lostandfound.FoundPowerBankActivity;
import com.example.lostandfound.FoundWatchActivity;
import com.example.lostandfound.FoundusbActivity;
import com.example.lostandfound.LostAudioActivity;
import com.example.lostandfound.LostBagActivity;
import com.example.lostandfound.LostBookActivity;
import com.example.lostandfound.LostChargerActivity;
import com.example.lostandfound.LostMobileActivity;
import com.example.lostandfound.LostOthersActivity;
import com.example.lostandfound.LostPowerBankActivity;
import com.example.lostandfound.LostWatchActivity;
import com.example.lostandfound.LostusbActivity;
import com.example.lostandfound.R;

public class FSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public FSliderAdapter(Context context){
        this.context=context;
    }

    private int[] slide_images= {

            R.drawable.wristwatch,
            R.drawable.speaker,
            R.drawable.smartphone,
            R.drawable.usb,
            R.drawable.book,
            R.drawable.backpack,
            R.drawable.charger,
            R.drawable.power_bank,
            R.drawable.help
    };

    private String[] slide_headings={
            "WATCH",
            "AUDIO DEVICES",
            "MOBILE",
            "USB DRIVES",
            "BOOK",
            "BAG",
            "CHARGERS",
            "POWER BANK",
            "OTHER Items"
    };

    public String[] slide_descriptions={
            "Select this option if you have found Smart Watch, Digital Watch, Analog Watch etc.",
            "Select this option if you have found audio devices like bluetooth speakers, earphones, headphones, airpods etc.",
            "Select this option if you have found any mobile, smartphone etc.",
            "Select this option if you have found any USB drives like Pendrive or Harddrive",
            "Select this option if you have found any Book",
            "Select this option if you have found any Bag, Backpack etc.",
            "Select this option if you have found any type of laptop charger or mobile charger etc.",
            "Select this option if you have found any Power Bank",
            "Select this option if you have found any other items other than previously mentioned items. "
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        final ImageButton mslideImageButton=(ImageButton)view.findViewById(R.id.slideImageButton);
        TextView mslideHeading=(TextView)view.findViewById(R.id.slideHeading);
        TextView mslideDescription=(TextView)view.findViewById(R.id.slideDescription);

        mslideImageButton.setImageResource(slide_images[position]);
        mslideHeading.setText(slide_headings[position]);
        mslideDescription.setText(slide_descriptions[position]);

        mslideImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0){
                    context.startActivity(new Intent(context, FoundWatchActivity.class));
                }
                else if(position==1) {
                    context.startActivity(new Intent(context, FoundAudioActivity.class));
                }
                else if(position==2) {
                    context.startActivity(new Intent(context, FoundMobileActivity.class));
                }
                else if(position==3){
                    context.startActivity(new Intent(context, FoundusbActivity.class));
                }
                else if(position==4){
                    context.startActivity(new Intent(context, FoundBookActivity.class));
                }
                else if(position==5){
                    context.startActivity(new Intent(context, FoundBagActivity.class));
                }
                else if(position==6){
                    context.startActivity(new Intent(context, FoundChargerActivity.class));
                }
                else if(position==7){
                    context.startActivity(new Intent(context, FoundPowerBankActivity.class));
                }
                else if(position==8){
                    context.startActivity(new Intent(context, FoundOthersActivity.class));
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
