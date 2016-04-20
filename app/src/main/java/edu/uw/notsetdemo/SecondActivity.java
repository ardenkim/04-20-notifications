package edu.uw.notsetdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "**Second**";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //action bar "back"
        // if hit, back to previous parent
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String value = extras.getString("edu.uw.notsetdemo.message");

            if(value != null) {
                TextView subtitle = (TextView) findViewById(R.id.txtSecond);
                subtitle.setText(value);

            }
        }

    }
}
