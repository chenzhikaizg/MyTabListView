package com.example.mytablistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyScreenView myScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myScreenView = (MyScreenView)findViewById(R.id.my_tabview);
        myScreenView.setAdapter(new MyMenuAdapter(this));
    }



}
