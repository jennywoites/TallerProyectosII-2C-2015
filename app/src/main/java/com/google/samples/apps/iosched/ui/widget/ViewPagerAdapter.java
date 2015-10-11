package com.google.samples.apps.iosched.ui.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by nicolas on 03/10/15.
 */
public abstract class ViewPagerAdapter extends FragmentStatePagerAdapter {

    protected CharSequence Titles[];
    protected int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumOfTabs;
    }

    @Override
    public abstract Fragment getItem(int position);

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
