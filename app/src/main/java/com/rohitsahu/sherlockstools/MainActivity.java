package com.rohitsahu.sherlockstools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
//import androidx.appcompat.widget.Toolbar;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      Toolbar toolbar =  findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
            switch(i){
                case 0:
                    //Caesar Cipher
                    Intent intentCaesar = new Intent(getApplicationContext(), CiptherActivity.class);
                    startActivity(intentCaesar);
                    break;
                case 1:
                    //Play Fair Cipher
                    Intent intentPlayfair = new Intent(getApplicationContext(), PlayFairCipher.class);
                    startActivity(intentPlayfair);
                    break;
                case 2:
                    // Hill Cipher
                    Intent intentHill = new Intent(getApplicationContext(), HillCipher.class);
                    startActivity(intentHill);
                    break;
                case 3:
                    // Rail Fence Technique
                    Intent intentRail = new Intent(getApplicationContext(), RailFence.class);
                    startActivity(intentRail);
                    break;
            }
        };

        ListView listView = findViewById(R.id.algo_list);
        listView.setOnItemClickListener(onItemClickListener);
    }
}