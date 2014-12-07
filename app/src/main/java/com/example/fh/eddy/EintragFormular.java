package com.example.fh.eddy;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Fabian on 09.11.2014.
 */
public class EintragFormular extends Activity {

    private EditText usersBz;
    private Button save_Button;

    // Kalendar anlegen
    private final Calendar c = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eintrag_formular);
        // The activity is being created.
        // Init Zeit und Datum TextViews
        TextView the_date = (TextView)findViewById(R.id.date_currentDate_textView);
        TextView the_time = (TextView) findViewById(R.id.time_currentTime_textView);
        // Für voreingestelltes Datum im Datum Edittext
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);
        the_date.setText(currentDay + "." + currentMonth + "." + currentYear);
        // Voreinstellung Uhrzeit, muss angepasst werden für Nullstellen
        // 24 Std. Uhr
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        the_time.setText(nullPad(currentHour) + ":" + nullPad(currentMinute));

    }

    //Hilfsfunktion Uhrzeit, Datum um Nullstellen richtig anzuzeigen
    public String nullPad(int timeDateInput)
    {
        if(timeDateInput > 10)
        {
            return String.valueOf(timeDateInput);
        }
        else
        {
            return "0"+ String.valueOf(timeDateInput);
        }
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