package com.example.appblockr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appblockr.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    TextView privacyPopup;


    public SliderAdapter(Context context){
        this.context = context;

    }

    public  int[] slide_images = {
            R.drawable.icon1,
            R.drawable.icon2,
            R.drawable.icon3
    };
    public  String[] slide_headings = {
            "Welcome to AppBlockr",
            "Simple blocker",
            "Free from distractions"
    };

    public  String[] slide_description = {
            "A simple way to block distractions from social media apps.",
            "Unlike other app blockers, AppBlockr will do what it's exactly made for.",
            "By continuing, you agree to our privacy policy"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.slide_img);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);
         privacyPopup = (TextView) view.findViewById(R.id.privacy_policy_link_popup);


        slideImage.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_description[position]);
        container.addView(view);

        return  view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    public void hidePrivacyPopup(){
        privacyPopup.setVisibility(View.INVISIBLE);
    }


    public void showPrivacyPopup(){
        privacyPopup.setVisibility(View.VISIBLE);
    }
}
