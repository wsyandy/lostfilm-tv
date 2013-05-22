package com.github.alexesprit.lostfilm.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.github.alexesprit.lostfilm.R;
import com.github.alexesprit.lostfilm.adapter.TabsAdapter;
import com.github.alexesprit.lostfilm.fragment.NewsListFragment;
import com.github.alexesprit.lostfilm.fragment.SerialsListFragment;

public class StartActivity extends SherlockFragmentActivity {
    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.start_view);
        setTitle(R.string.app_title);

        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager pager = (ViewPager)findViewById(R.id.start_view_pager);
        TabsAdapter adapter = new TabsAdapter(this, pager);
        adapter.addTab(bar.newTab().setText(R.string.new_series), NewsListFragment.class);
        adapter.addTab(bar.newTab().setText(R.string.all_series), SerialsListFragment.class);

        if (null != inState) {
            int pos = inState.getInt("pos", 0);
            pager.setCurrentItem(pos);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int pos = getSupportActionBar().getSelectedTab().getPosition();
        outState.putInt("pos", pos);
    }
}
