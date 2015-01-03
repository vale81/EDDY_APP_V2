package com.example.fh.eddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;



/**
 * Created by Tim  Nov. 2014.
 */
public class EintragFormular extends Activity {

    // Database object with helper
    private DataHandler myDataHandler;
    // Init calendar
    private final Calendar c = Calendar.getInstance();
    // Init spinners
    private Spinner eventSpinner;
    private Spinner activitySpinner;
    // Init fields globally so they can be manipulated
    TextView the_date;
    TextView the_time;
    EditText currentBloodsugarlevel;
    EditText currentMealCarbAmount;
    EditText currentBolusInsulin;
    EditText currentBaseInsulin;

    // Init buttons
    ImageButton saveNewEntry;
    ImageButton cancelNewEntry;
    EintragDaten entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eintrag_formular);
        // The activity is being created.
        the_date = (TextView)findViewById(R.id.date_currentDate_textView);
        the_time = (TextView) findViewById(R.id.time_currentTime_textView);
        currentBloodsugarlevel = (EditText) findViewById(R.id.BZ_editText);
        currentMealCarbAmount = (EditText) findViewById(R.id.mahlzeit_EditText);
        currentBolusInsulin =(EditText) findViewById(R.id.bolus_editText);
        currentBaseInsulin = (EditText) findViewById(R.id.basis_editText);

        // Setting the date in the textviews
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        // Java month starts at 0, thus need for adding 1 to values
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentYear = c.get(Calendar.YEAR);
        the_date.setText(currentDay + "." + currentMonth + "." + currentYear);
        // Preset current time, needs to be adjusted with 0-padding for correct 24-Hour format
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        // Calling helper method to correctly set 0-padding
        the_time.setText(nullPad(currentHour) + ":" + nullPad(currentMinute));


        // Filling the activity spinner with selectable activities and adding listener
        fillActivitySpinner();
        addListenerToActivitySpinner();

        // Filling the event spinner with events and adding listener
        fillEventSpinner();
        addListenerToEventSpinner();


        // Init the save button and setting save button listener
        saveNewEntry = (ImageButton) findViewById(R.id.save_Button);
        saveNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if user entered a bloodsugar value, required
                if (currentBloodsugarlevel.getText().toString().length() == 0)
                {
                    currentBloodsugarlevel.setError("Bitte geben Sie einen Blutzuckerwert ein.");
                }
                // Validation passed, execute operations
                else
                {
                int bloodSugarValue = Integer.valueOf(currentBloodsugarlevel.getText().toString());
                String currentBolus = currentBolusInsulin.getText().toString();
                String baseInsulin = currentBaseInsulin.getText().toString();
                String mealCarbAmount = currentMealCarbAmount.getText().toString();
                String currTime = the_time.getText().toString();
                String currDate = the_date.getText().toString();


                // Get the spinner value for the activity spinner
                String spinnerSelectedValue = ((Spinner)findViewById(
                        R.id.aktivitaet_spinner)).getSelectedItem().toString();

                // Get the spinner value for the event spinner
                String eventSpinnerSelectedValue = ((Spinner)findViewById(
                        R.id.event_spinner)).getSelectedItem().toString();

                // Database operations
                myDataHandler = new DataHandler(getBaseContext());
                myDataHandler.open();
                entry = myDataHandler.insertNewData(bloodSugarValue,currentBolus,
                        baseInsulin,mealCarbAmount, currTime , currDate , spinnerSelectedValue, eventSpinnerSelectedValue,new Date().getTime());

                    // Return to main screen and clear stack via flag
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // Toast for user feedback
                    Toast toast=Toast.makeText(getApplicationContext(),"Eintrag gespeichert.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                }

            }
        }); // End onClick saveButton

        // Init cancel button and setting cancel button listener
        cancelNewEntry = (ImageButton) findViewById(R.id.cancel_Button);
        cancelNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        }); // End onClick CancelButton



    } // end onCreate()

    // Method for filling the activity spinner with activities
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
        // Adapter for spinner contents
        ArrayAdapter<String> activitySpinnerAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
        activitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the spinner's adapter
        activitySpinner.setAdapter(activitySpinnerAdapter);

    } // End fillActivitySpinner()

    // Method for filling the event spinner with events
    public void fillEventSpinner()
    {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        eventSpinner = (Spinner) findViewById(R.id.event_spinner);

        List<String> list = new ArrayList<String>();

        Set<String> s = myPrefs.getStringSet("events", new HashSet<String>());

        Iterator it= s.iterator();

        String[] array= getResources().getStringArray(R.array.default_events);
        for (String item :array) {
            list.add(item);
        }
        while(it.hasNext()) {
            String item=(String)it.next();
            list.add(item);
        }
        // Adapter for spinner contents
        ArrayAdapter<String> eventSpinnerAdapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, list);
        eventSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the spinner's adapter
        eventSpinner.setAdapter(eventSpinnerAdapter);

    } // End fillEventSpinner()

    // Method to add listener to activity spinner
    public void addListenerToActivitySpinner()
    {
        activitySpinner = (Spinner) findViewById(R.id.aktivitaet_spinner);

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
              // String activitySpinnerItemPicked = activitySpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                activitySpinner.setSelection(1);
            }
        });
    } // End add listener to activity spinner

    // Method to add listener to event spinner
    public void addListenerToEventSpinner()
    {
        eventSpinner = (Spinner) findViewById(R.id.aktivitaet_spinner);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // String eventSpinnerItemPicked = eventSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                eventSpinner.setSelection(1);            }
        });
    } // End add listener to event spinner

    // Helper method for correctly setting 0-padding in 24-Hour format
    public String nullPad(int timeDateInput)
    {
        if(timeDateInput >= 10)
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