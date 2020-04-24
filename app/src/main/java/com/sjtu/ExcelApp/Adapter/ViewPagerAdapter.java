package com.sjtu.ExcelApp.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> pages;
    private ViewPager viewPager;
    public ViewPagerAdapter(List<View> pages, ViewPager viewPager) {
        this.pages = pages;
        this.viewPager = viewPager;
    }
    @Override
    public int getCount() {
        return pages.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = pages.get(position % pages.size());
        viewPager.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        viewPager.removeView(pages.get(position % pages.size()));
    }
}
