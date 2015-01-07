package com.example.fh.eddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Tim on 07.01.2015.
 */
public class UserDataOverview extends Activity {

    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_layout);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        TextView username = (TextView) findViewById(R.id.reset_userName_text);
        TextView carbSetting = (TextView) findViewById(R.id.reset_carbSetting_text);
        TextView upperBloodsugarBound = (TextView) findViewById(R.id.reset_upperBloodSugarValue_text);
        TextView lowerBloodsugarBound = (TextView) findViewById(R.id.reset_lowerBloodSugarValue_text);
        TextView bolusInsulin = (TextView) findViewById(R.id.reset_BolusInsulinValue_text);
        TextView baseInsulin = (TextView) findViewById(R.id.reset_BaseInsulinValue_text);
        TextView correctionFactor = (TextView) findViewById(R.id.reset_CorrectionFactorValue_textView);

        username.setText(sharedPrefs.getString(getString(R.string.user_name_preference_key), ""));
        carbSetting.setText(sharedPrefs.getString(getString(R.string.mahlzeit_angabe_preference_key), ""));
        upperBloodsugarBound.setText(sharedPrefs.getString(getString(R.string.obere_blutzuckergrenze_preference_key), ""));
        lowerBloodsugarBound.setText(sharedPrefs.getString(getString(R.string.untere_blutzuckergrenze_preference_key), ""));
        bolusInsulin.setText(sharedPrefs.getString(getString(R.string.bolus_insulin_preference_key), ""));
        baseInsulin.setText(sharedPrefs.getString(getString(R.string.basis_insulin_preference_key), ""));
        correctionFactor.setText(sharedPrefs.getString(getString(R.string.korrektur_factor_preference_key), ""));

        TextView activity = (TextView) findViewById(R.id.reset_activity_texts);
        TextView event = (TextView) findViewById(R.id.reset_event_texts);
        TextView medicine = (TextView) findViewById(R.id.reset_medicine_texts);
        TextView factor = (TextView) findViewById(R.id.reset_factor_texts);

        showActivityUserSettings(activity);
        showEventUserSettings(event);
        showMedicineUserSettings(medicine);
        showFactorUserSettings(factor);
    }

    /**
     * Retrieves the Data of all the Preferences and Displays them
     *
     */
    private void showActivityUserSettings(TextView txtview) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        Set<String> s = sharedPrefs.getStringSet(getString(R.string.activity_preference_key), new HashSet<String>());
        Iterator it= s.iterator();

        while (it.hasNext())
        {
            String item=(String)it.next();
            builder.append(item+" ");
            builder.append("\n");

        }
        txtview.setText(builder.toString());
    }
    private void showEventUserSettings(TextView txtview) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        Set<String> s = sharedPrefs.getStringSet(getString(R.string.event_preference_key), new HashSet<String>());
        Iterator it= s.iterator();

        while (it.hasNext())
        {
            String item=(String)it.next();
            builder.append(item+" ");
            builder.append("\n");

        }
        txtview.setText(builder.toString());
    }
    private void showMedicineUserSettings(TextView txtview) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        Set<String> s = sharedPrefs.getStringSet(getString(R.string.medicin_preference_key), new HashSet<String>());
        Iterator it= s.iterator();

        while (it.hasNext())
        {
            String item=(String)it.next();
            builder.append(item+" ");
            builder.append("\n");

        }
        txtview.setText(builder.toString());
    }
    private void showFactorUserSettings(TextView txtview) {

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        Set<String> s = sharedPrefs.getStringSet(getString(R.string.befactor_preference_key), new HashSet<String>());
        Iterator it= s.iterator();

        while (it.hasNext())
        {
            String item=(String)it.next();
            builder.append(item+" ");
            builder.append("\n");

        }
        txtview.setText(builder.toString());
    }





}
