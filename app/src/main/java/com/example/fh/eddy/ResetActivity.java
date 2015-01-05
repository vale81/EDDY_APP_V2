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
 * Created by Fabian on 02.01.2015.
 */
public class ResetActivity extends Activity {

    private DataHandler myDataHandler;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_layout);
        context=this;
        TextView textview = (TextView) findViewById(R.id.reset_text);
        showUserSettings(textview);
    }

    private void showUserSettings(TextView txtview) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        builder.append(getString(R.string.overview)+"\n\n");


        Set<String> s = sharedPrefs.getStringSet("activities", new HashSet<String>());
        builder.append(getString(R.string.activities)+": \n");
        Iterator it= s.iterator();
        while(it.hasNext()) {
            String item=(String)it.next();
            builder.append(item+", ");
        }
        builder.append("\n");

        s = sharedPrefs.getStringSet("events", new HashSet<String>());
        builder.append(getString(R.string.events)+": \n");
        it= s.iterator();

        while (it.hasNext()) {
            String item = (String) it.next();
            builder.append(item + ", ");
            }
            builder.append("\n");

        s = sharedPrefs.getStringSet("medicines", new HashSet<String>());
        builder.append(getString(R.string.medicins)+": \n");
        it= s.iterator();
        while(it.hasNext()) {
            String item=(String)it.next();
            builder.append(item+", ");
        }
        builder.append("\n");

        s = sharedPrefs.getStringSet("be_factor", new HashSet<String>());
        builder.append(getString(R.string.be_factors)+": \n");
        it= s.iterator();
        while(it.hasNext()) {
            String item=(String)it.next();
            builder.append(item+", ");
        }
        builder.append("\n");

        builder.append("\n");

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


        txtview.setText(builder.toString());
    }


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

    private void restartThis() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


}
