package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class secondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void click(View v){
        EditText input = findViewById(R.id.txt);
        String str = input.getText().toString();
        float s = Float.parseFloat(str);
        float r = s*1.8f+32;

        TextView show = findViewById(R.id.show_tmp);
        show.setText(getString(R.string.txt_result) + String.format("%.2f",r));

    }
}
