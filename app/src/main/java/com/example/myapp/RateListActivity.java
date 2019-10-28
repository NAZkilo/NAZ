package com.example.myapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class RateListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String data[]=("one","two","three");

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);
    }
}
