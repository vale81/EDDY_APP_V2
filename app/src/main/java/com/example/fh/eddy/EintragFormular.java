package com.example.fh.eddy;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

/**
 * Created by Fabian on 09.11.2014.
 */
public class EintragFormular extends Activity {

    private EditText usersBz;
    private Button save_Button;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eintrag_formular);
        // The activity is being created.
        // Kalender Objekt für Datum und Uhrzeit Voreinstellung
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR);
        int currentMinute = c.get(Calendar.MINUTE);
        // Voreingestellte Uhrzeit
        EditText the_time = (EditText) findViewById(R.id.time_editText);
        the_time.setText(currentHour + ":" + currentMinute);
        // Voreingestelltes Datum im Datum Edittext
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);
        EditText the_date = (EditText)findViewById(R.id.date_editText);
        the_date.setText(currentDay + "." + currentMonth + "." + currentYear);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }
}