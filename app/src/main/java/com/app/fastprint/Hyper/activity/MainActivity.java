package com.app.fastprint.Hyper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fastprint.R;
import com.oppwa.mobile.connect.provider.Connect;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);


        ((TextView) findViewById(R.id.version_number))
                .setText(String.format(getString(R.string.sdk_version_number), Connect.getVersion()));
    }


}
