package com.example.android.guardianapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class GuardianAdapter extends ArrayAdapter<Guardian> {


    public GuardianAdapter(@NonNull Context context, List<Guardian> guardians) {
        super(context, 0,guardians);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView= convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Guardian guardian=getItem(position);

        TextView webTitle =listItemView.findViewById(R.id.webTitle);
        webTitle.setText(guardian.getWebTitle());


        TextView sectionName =listItemView.findViewById(R.id.sectionName);
        sectionName.setText(guardian.getSectionName());

        return listItemView;
    }
}
