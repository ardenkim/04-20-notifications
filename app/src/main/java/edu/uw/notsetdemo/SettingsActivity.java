package edu.uw.notsetdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// in order to show the preferences! we need activity
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
