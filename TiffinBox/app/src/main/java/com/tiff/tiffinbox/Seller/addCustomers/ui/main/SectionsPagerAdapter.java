package com.tiff.tiffinbox.Seller.addCustomers.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.AddSeller;
import com.tiff.tiffinbox.Seller.ViewSeller2;
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomer;
import com.tiff.tiffinbox.Seller.addCustomers.yourCustomers;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_textcustomer_1, R.string.tab_textcustomer_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
   //     return PlaceholderFragment.newInstance(position + 1);
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new PlaceholderFragment();
                break;
            case 1:
                fragment = new yourCustomers();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}