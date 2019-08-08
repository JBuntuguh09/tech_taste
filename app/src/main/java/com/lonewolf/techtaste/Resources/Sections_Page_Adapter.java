package com.lonewolf.techtaste.Resources;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.lonewolf.techtaste.Fragments.Frag_Orders;
import com.lonewolf.techtaste.Fragments.Frag_Send_Request;
import com.lonewolf.techtaste.Fragments.Frag_Settings;

public class Sections_Page_Adapter extends FragmentPagerAdapter {


    public Sections_Page_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new Frag_Send_Request();

            case 1:
                return new Frag_Orders();

            case 2:
                return new Frag_Settings();
             default :
                 return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Request";

            case 1:
                return "My Orders";

            case 2:
                return "Settings";

            default:
                return null;

        }
    }




}
