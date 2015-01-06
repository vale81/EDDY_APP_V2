package com.example.fh.eddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Fabian Tim
 */
public class ResetActivity extends Activity {

    private DataHandler myDataHandler;
    private Context context;
    SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_layout);
        context=this;
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

     /*






        builder.append(getString(R.string.name)+": \n");
        builder.append(sharedPrefs.getString("user_name", ""));
        builder.append("\n");
        builder.append(getString(R.string.meal_display)+": \n");
        builder.append(sharedPrefs.getString("mahlzeit_angabe", ""));
        builder.append("\n");
        builder.append(getString(R.string.upper_bloodsuger_bound)+": \n");
        builder.append(sharedPrefs.getString("obere_blutzuckergrenze", ""));
        builder.append("\n");
        builder.append(getString(R.string.lower_bloodsuger_bound)+": \n");
        builder.append(sharedPrefs.getString("untere_blutzuckergrenze", ""));
        builder.append("\n");
        builder.append(getString(R.string.basis_insulin)+": \n");
        builder.append(sharedPrefs.getString("basis_insulin", ""));
        builder.append("\n");
        builder.append(getString(R.string.bolus_insulin)+": \n");
        builder.append(sharedPrefs.getString("bolus_insulin", ""));
        builder.append("\n");
        builder.append(getString(R.string.compensation_factor)+": \n");
        builder.append(sharedPrefs.getString("korrektur_factor", ""));
        builder.append("\n");
*/

  //      txtview.setText(builder.toString());
   // }

    /**
     * Creates and Shows the AlertDialog when the User attempts to Reset all Data
     * If the User accepts, every Preference will be deleted and Default Values are loaded
     *
     */
    public void showDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_all));

        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .edit()
                        .clear()
                        .commit();
                PreferenceManager.setDefaultValues(context, R.xml.preferences, true);
                myDataHandler = new DataHandler(getBaseContext());
                myDataHandler.open();
                myDataHandler.deleteAllEntries();
                restartThis();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.abort), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    //Finishes the Reset Process of the Preferences and Displays the Activity again
    private void restartThis() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


}
