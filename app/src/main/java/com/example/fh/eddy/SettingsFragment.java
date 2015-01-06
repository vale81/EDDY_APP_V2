package com.example.fh.eddy;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Setting Fragment
 * Load the Preference from XML Resource
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

    }

}

