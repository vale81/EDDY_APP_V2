package com.example.fh.eddy;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by Fabian on 09.11.2014.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}

