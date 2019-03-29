package com.marolix.dailynews;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAdapter extends FragmentPagerAdapter {
    private Context context;
    private String desc, title;
    ArrayList<Map<String,String>> al=new ArrayList<>();
    public MyAdapter(FragmentManager fm, Context context, ArrayList<Map<String,String>> al) {
        super(fm);
        this.al=al;
        this.context = context;

    }

    @Override
    public Fragment getItem(int i) {

        if (i <= getCount()) {

            return MyFragment.newInstance(al.get(i));
        }

        return null;
    }

    @Override
    public int getCount() {

        return al.size();
    }
}
