package edu.uw.notsetdemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by ArdenKim on 4/20/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
