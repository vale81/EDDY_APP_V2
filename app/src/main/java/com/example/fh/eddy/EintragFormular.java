package com.example.fh.eddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import org.w3c.dom.Text;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Tim  Nov. 2014.
 */
public class EintragFormular extends Activity {

    // EintragDaten Objekt
    private EintragDaten neuerEintrag;
    // Database Objekt mit Helper
    private DataHandler myDataHandler;
    // Kalendar anlegen
    private final Calendar c = Calendar.getInstance();
    // Spinner anlegen
    private Spinner activitySpinner;
    // Edittextfelder anlegen


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eintrag_formular);
        // The activity is being created.
        // Init alle EditTexts nach Reihenfolge auf Bildschirm, Spinner separat
        TextView the_date = (TextView)findViewById(R.id.date_currentDate_textView);
        TextView the_time = (TextView) findViewById(R.id.time_currentTime_textView);
        EditText currentBlutzucker = (EditText) findViewById(R.id.BZ_editText);
        EditText currentMahlzeit = (EditText) findViewById(R.id.mahlzeit_EditText);
        EditText currentBolus =(EditText) findViewById(R.id.bolus_editText);
        EditText currentBasis = (EditText) findViewById(R.id.basis_editText);
        EditText currentNotiz = (EditText) findViewById(R.id.notiz_editText);
        // Init Buttons
        ImageButton saveButton = (ImageButton) findViewById(R.id.save_Button);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.cancel_Button);

        // Spinner mit Aktivitaeten fuellen
        fillActivitySpinner();
        // Listener zu Spinner hinzufuegen
        addListenerToaktivitaetSpinner();

        // Für voreingestelltes Datum im Datum Edittext
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        // Java Monat Januar = 0 , daher + 1
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentYear = c.get(Calendar.YEAR);
        the_date.setText(currentDay + "." + currentMonth + "." + currentYear);
        // Voreinstellung Uhrzeit, muss angepasst werden für Nullstellen
        // 24 Std. Uhr
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        the_time.setText(nullPad(currentHour) + ":" + nullPad(currentMinute));

    } // end onCreate

    // Methode zum Befuellen des Spinners
    public void fillActivitySpinner()
    {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        activitySpinner = (Spinner) findViewById(R.id.aktivitaet_spinner);

        List<String> list = new ArrayList<String>();

        Set<String> s = myPrefs.getStringSet("activities", new HashSet<String>());

        Iterator it= s.iterator();

        String[] array= getResources().getStringArray(R.array.default_activities);
        for (String item :array) {
            list.add(item);
        }
        while(it.hasNext()) {
            String item=(String)it.next();
            list.add(item);
        }


        // Adapter fuer Spinner Inhalt
        ArrayAdapter<String> activitySpinnerAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
        activitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set Adapter
        activitySpinner.setAdapter(activitySpinnerAdapter);

    }
    // Spinner Listener
    public void addListenerToaktivitaetSpinner()
    {
        activitySpinner = (Spinner) findViewById(R.id.aktivitaet_spinner);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
               String aktivitaetGeweahlt = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // TODO Auto-generated
            }
        });
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