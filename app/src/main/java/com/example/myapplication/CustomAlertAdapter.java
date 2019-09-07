package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAlertAdapter extends BaseAdapter {
    Context ctx = null;
    ArrayList<String> listarray = null;
    private LayoutInflater mInflater = null;

    public CustomAlertAdapter(Activity activity, ArrayList<String> list) {
        this.ctx = activity;
        mInflater = activity.getLayoutInflater();
        this.listarray = list;

    }

    public int getCount() {
        return listarray.size();
    }

    public Object getItem(int arg) {
        return listarray.get(arg);
    }

    public long getItemId(int arg) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.alertlistrow,null);
            holder.titlename = (TextView) convertView.findViewById(R.id.textView_titlename);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        String datavalue = listarray.get(position);
        holder.titlename.setText(datavalue);
        return convertView;

    }

    private static class ViewHolder {
        TextView titlename;
    }
}
