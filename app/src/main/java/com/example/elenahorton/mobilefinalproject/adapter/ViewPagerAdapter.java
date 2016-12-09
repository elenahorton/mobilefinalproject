package com.example.elenahorton.mobilefinalproject.adapter;

/**
 * Created by elenahorton on 12/9/16.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.elenahorton.mobilefinalproject.fragments.ByLocationFragment;
import com.example.elenahorton.mobilefinalproject.fragments.ByUserFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ByLocationFragment();
                break;
            case 1:
                fragment = new ByUserFragment();
                break;
            default:
                fragment = new ByLocationFragment();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Near You";
            case 1:
                return "Your Posts";
        }

        return "Near You";
    }

    @Override
    public int getCount() {
        return 2;
    }
}