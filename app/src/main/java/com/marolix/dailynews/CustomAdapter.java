package com.marolix.dailynews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] arr;

    public CustomAdapter(Context context, String[] arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View customView;
        customView = LayoutInflater.from(context).inflate(R.layout.list, viewGroup, false);
        TextView ping = customView.findViewById(R.id.name);
        ping.setText(arr[i]);

        return customView;
    }
    public void setChecked(View v, int position) {

        ViewGroup parent = ((ViewGroup) v.getParent()); // linear layout

        for (int x = 0; x < parent.getChildCount() ; x++) {

            View cv = parent.getChildAt(x);
            ((RadioButton)cv.findViewById(R.id.radiobutton)).setChecked(false); // your checkbox/radiobtt id
        }

        RadioButton radio = (RadioButton)v.findViewById(R.id.checkbox);
        radio.setChecked(true);
    }
}
