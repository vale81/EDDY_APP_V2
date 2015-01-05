package com.example.fh.eddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
public class NewEntryForm extends Activity {

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
    TextView mealUnitText;
    EditText currentBloodsugarlevel;
    EditText currentMealCarbAmount;
    EditText currentBolusInsulin;
    EditText currentBaseInsulin;

    // Init buttons
    ImageButton saveNewEntry;
    ImageButton cancelNewEntry;
    // Entry object
    EntryData entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_form);
        // The activity is being created.


        // Database operations
        myDataHandler = new DataHandler(getBaseContext());
        myDataHandler.open();

        the_date = (TextView)findViewById(R.id.date_currentDate_textView);
        the_time = (TextView) findViewById(R.id.time_currentTime_textView);
        mealUnitText = (TextView) findViewById(R.id.kohlenhydratMenge_textView);
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

        setMealUnitText();


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
                    currentBloodsugarlevel.setError(getString(R.string.error_message_if_bloodsugar_empty));
                    currentBloodsugarlevel.requestFocus();
                    /* Show the keyboard after edit text is refocused because of error
                    /* Necessary because keyboard does not automatically reappear if user hits
                       Save button on empty form */
                    InputMethodManager myInputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    myInputMethodManager.showSoftInput(currentBloodsugarlevel, InputMethodManager.SHOW_IMPLICIT);
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

                // Insert new data into database
                entry = myDataHandler.insertNewData(bloodSugarValue,currentBolus,
                        baseInsulin,mealCarbAmount, currTime , currDate , spinnerSelectedValue, eventSpinnerSelectedValue,new Date().getTime());

                    // Return to main screen and clear stack via flag
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // Toast for user feedback
                    Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.toast_new_entry_saved), Toast.LENGTH_LONG);
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
                // Nothing done here
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
                // Nothing done here
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

    public void setMealUnitText ()
    {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String s = myPrefs.getString("mahlzeit_angabe", "KH");
        mealUnitText.setText(s);
    }

}