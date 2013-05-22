package com.github.alexesprit.lostfilm.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final Context context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;

    public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        context = activity;
        actionBar = activity.getSupportActionBar();
        viewPager = pager;
        viewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> cls) {
        tab.setTag(cls.getName());
        tab.setTabListener(this);
        actionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return actionBar.getTabCount();
    }

    @Override
    public Fragment getItem(int position) {
        String fragmentName = (String)actionBar.getTabAt(position).getTag();
        return Fragment.instantiate(context, fragmentName);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
