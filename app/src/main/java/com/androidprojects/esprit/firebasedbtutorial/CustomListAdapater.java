package com.androidprojects.esprit.firebasedbtutorial;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amal on 12/02/2017.
 */

public class CustomListAdapater extends BaseAdapter {

    private ArrayList<String> names;
    private ArrayList<Integer> phoneNumbers;
    private AppCompatActivity activity;

    private int x=0;


    public CustomListAdapater(ArrayList<String> names,ArrayList<Integer> phoneNumbers,AppCompatActivity activity){
        this.names=names;
        this.phoneNumbers=phoneNumbers;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =  LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.people_list_row,viewGroup,false);
        ((TextView)view.findViewById(R.id.personPhone)).setText(String.valueOf(phoneNumbers.get(i)));
        ((TextView)view.findViewById(R.id.personNameTv)).setText(names.get(i));
        return view;
    }
}
