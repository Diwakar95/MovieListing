package com.example.shailu.movieslisting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shailu on 3/8/16.
 */

public class InformationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView dvTextView;
    private ImageView profilepic;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dvTextView = (TextView) findViewById(R.id.developer_tv);
        nameTextView = (TextView) findViewById(R.id.name_tt);



    }
}
