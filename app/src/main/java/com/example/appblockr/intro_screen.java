package com.example.appblockr;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.appblockr.adapter.SliderAdapter;

public class intro_screen extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private Button nextBtn;
    private Button backBtn;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.activity_intro_screen);
        getSupportActionBar().hide();
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.layout_dots);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        nextBtn.setEnabled(true );
        backBtn.setEnabled(false);
        backBtn.setVisibility(View.INVISIBLE);
        nextBtn.setText("Next");
        backBtn.setText("");
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.lightBlue));
            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;

            if(position == 0){
                nextBtn.setEnabled(true );
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Next");
                backBtn.setText("");
            } else if (position == 1){
                nextBtn.setEnabled(true );
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Next");
                backBtn.setText("Back");
            } else {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Accept");
                backBtn.setText("");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}