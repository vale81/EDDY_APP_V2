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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Tim on 30.12.2014.
 * This class is basically a copy of the EntryForm with
 * values set according to the database.
 */
public class EditEntryForm extends Activity {

    // Database object with helper
    private DataHandler myDataHandler;
    // Init spinners
    private Spinner eventSpinner;
    private Spinner activitySpinner;
    // Same fields as in EntryForm
    TextView the_date;
    TextView the_time;
    TextView mealUnitText;
    EditText currentBloodsugarlevel;
    EditText currentMealCarbAmount;
    EditText currentBolusInsulin;
    EditText currentBaseInsulin;
    // Init buttons
    ImageButton updateEntryButton;
    ImageButton discardUpdateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Database operation on start of activity
        // Only open database once -- expensive
        // Kernel closes database automatically
        myDataHandler = new DataHandler(this);
        myDataHandler.open();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_entry_form_layout);

        // Get the passed extras via getIntent()
        final EntryData current_Edit_Entry = (EntryData)getIntent().getSerializableExtra("passedVar");

        the_date = (TextView)findViewById(R.id.date_currentDate_textView);
        the_time = (TextView) findViewById(R.id.time_currentTime_textView);
        mealUnitText = (TextView) findViewById(R.id.kohlenhydratMenge_textView);
        currentBloodsugarlevel = (EditText) findViewById(R.id.BZ_editText);
        currentMealCarbAmount = (EditText) findViewById(R.id.mahlzeit_EditText);
        currentBolusInsulin =(EditText) findViewById(R.id.bolus_editText);
        currentBaseInsulin = (EditText) findViewById(R.id.basis_editText);

        // Set meal unit textView according to stored preference
        setMealUnitText();

        // Preset the forms fields with values received from entry selected in ListView
        currentBloodsugarlevel.setText(String.valueOf(current_Edit_Entry.getBloodSugarValue()));
        currentBaseInsulin.setText(String.valueOf(current_Edit_Entry.getBaseInsulin()));
        currentBolusInsulin.setText(String.valueOf(current_Edit_Entry.getBolus()));
        currentMealCarbAmount.setText(String.valueOf(current_Edit_Entry.getCarbAmount()));
        the_date.setText(String.valueOf(current_Edit_Entry.getTheDate()));
        the_time.setText(String.valueOf(current_Edit_Entry.getDaytime()));

        // Filling the activity spinner with selectable activities and adding listener
        fillActivitySpinner();
        addListenerToActivitySpinner();
        activitySpinner.setSelection(getActivitySpinnerIndex(activitySpinner, current_Edit_Entry.getActivity()));

        // Filling the event spinner with events and adding listener
        fillEventSpinner();
        addListenerToEventSpinner();
        eventSpinner.setSelection(getEventSpinnerIndex(eventSpinner, current_Edit_Entry.getEvent()));

        // Init the update button and setting update button listener
        updateEntryButton = (ImageButton) findViewById(R.id.save_Button);
        updateEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if user entered a bloodsugar value, required
                if (currentBloodsugarlevel.getText().toString().length() == 0)
                {
                    currentBloodsugarlevel.setError(getString(R.string.error_message_if_bloodsugar_empty));
                    currentBloodsugarlevel.requestFocus();
                }
                else
                {
                    // Validation passed, execute operations
                    int bloodSugarValue = Integer.valueOf(currentBloodsugarlevel.getText().toString());
                    String currentBolus = currentBolusInsulin.getText().toString();
                    String baseInsulin = currentBaseInsulin.getText().toString();
                    String mealCarbAmount = currentMealCarbAmount.getText().toString();
                    String currTime = the_time.getText().toString();
                    String currDate = the_date.getText().toString();

                    // Get the spinner value for the activity spinner
                    String spinnerSelectedValue = ((Spinner) findViewById(
                            R.id.aktivitaet_spinner)).getSelectedItem().toString();

                    // Get the spinner value for the event spinner
                    String eventSpinnerSelectedValue = ((Spinner) findViewById(
                            R.id.event_spinner)).getSelectedItem().toString();

                    // Database update
                    myDataHandler.updateSingleEntry(current_Edit_Entry.getId(), bloodSugarValue, currentBolus,
                            baseInsulin, mealCarbAmount, currTime, currDate, spinnerSelectedValue, eventSpinnerSelectedValue);

                    // Toast for user feedback
                    Toast toast = Toast.makeText(getApplicationContext(), "Eintrag geändert.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    // Return to main screen and clear stack via flag
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }); // End onClick saveButton

        // Init cancel button and setting cancel button listener
        discardUpdateButton = (ImageButton) findViewById(R.id.cancel_Button);
        discardUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCancelDialog();

            }
        }); // End onClick CancelButton
    } // End onCreate()

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
                String activitySpinnerItemPicked = activitySpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // If nothing selected, set default to item 1
                activitySpinner.setSelection(1);
            }
        });
    } // End add listener to activity spinner

    // Method to add listener to event spinner
    public void addListenerToEventSpinner()
    {
        eventSpinner = (Spinner) findViewById(R.id.event_spinner);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String eventSpinnerItemPicked = eventSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                eventSpinner.setSelection(1);
            }
        });
    } // End add listener to event spinner


    // Helper method to correctly set event spinner entry based on entry in database
    private int getEventSpinnerIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    // Helper method to correctly set activity spinner entry based on entry in database
    private int getActivitySpinnerIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    // Method to show AlertDialog when user hits cancel button in EditEntryForm
    public void showCancelDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.cancel_Update_Entry));

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                // Return to MainScreen if user cancels updating the entry
                Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Toast toast= Toast.makeText(getApplicationContext(),"Änderungen verworfen.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Simply dismiss the dialog on negative user choice
                dialog.dismiss();
            }
        });

        builder.show();
    } //End showCancelDialog

    public void setMealUnitText ()
    {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String s = myPrefs.getString("mahlzeit_angabe", "KH");
        mealUnitText.setText(s);
    }



}







