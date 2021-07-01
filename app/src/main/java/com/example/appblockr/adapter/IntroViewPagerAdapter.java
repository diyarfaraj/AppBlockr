package com.example.appblockr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appblockr.R;
import com.example.appblockr.ScreenItem;

import java.util.List;

public class IntroViewPagerAdapter  extends PagerAdapter {

    Context mContext;
    List<ScreenItem> listScreen;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> listScreen) {
        this.mContext = mContext;
        this.listScreen = listScreen;
    }

    @Override
    public int getCount() {
        return listScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen, null);
        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        TextView titel = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_description);
        titel.setText(listScreen.get(position).getTitle());
        description.setText(listScreen.get(position).getDescription());
        imgSlide.setImageResource(listScreen.get().getScreenImg());
        container.addView(layoutScreen);
        return layoutScreen;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);

        container.removeView((View)object);
    }
}
