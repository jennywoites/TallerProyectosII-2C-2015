package com.google.samples.apps.iosched.ui.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.MascotaDetalleComentariosFragment;
import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.MascotaDetalleDescripcionFragment;

/**
 * Created by nicolas on 03/10/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MascotaDetalleDescripcionFragment();
            case 1:
                return new MascotaDetalleComentariosFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
