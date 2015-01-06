package com.example.fh.eddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * MealFactor Manager Class
 * Allows the User to manage his MealFactors
 * @author Fabian
 */
public class MealFactorManager extends Activity {

    private SharedPreferences.Editor editor;

    private Set<String> s;
    private ArrayList<String> list;
    private ArrayAdapter adapter;

    /**
     * On Create Method initialises the Activity
     * Load the Layout, get Preferences, set ClickListener
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_layout);
        TextView txtview = (TextView) findViewById(R.id.textview_verwaltung);
        txtview.setText(R.string.be_verwaltung_text);
        Button button=(Button)findViewById(R.id.new_Item);
        button.setText(R.string.be_verwaltung_button);
        EditText txtfield=(EditText) findViewById(R.id.textfield_verwaltung);
        txtfield.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        //Retrieve the Items of the specified Preference
        s = new HashSet<String>(sharedPrefs.getStringSet(getString(R.string.befactor_preference_key), new HashSet<String>()));
        ListView listview = (ListView) findViewById(R.id.listView);
        list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview_layout, R.id.firstLine, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                showDialog((String) parent.getItemAtPosition(position));
            }
        });

    }

    /**
     * Creates and Shows the AlertDialog when the User attempts to Delete an Item
     * @param text Item that should be deleted
     */
    public void showDialog(final String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_Be) +" "+ text);

        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //Delete Item from List and Preference
                list.remove(text);
                s.remove(text);
                editor.putStringSet(getString(R.string.befactor_preference_key),s);
                editor.commit();
                adapter.notifyDataSetChanged();
                // Toast for user feedback
                Toast toast=Toast.makeText(getApplicationContext(),getString(R.string.be_factor)+" "+text+" "+getString(R.string.deleted), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();

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

    /**
     * Method that gets called when the user presses the Save Button
     * Adds the entered Data to the Preferences
     *
     */
    public void saveItem(View view) {

        //Get User input (the New item)
        EditText textfield= (EditText)findViewById(R.id.textfield_verwaltung);
        String activity=textfield.getText().toString();

        //Add the new Item
        s.add(activity);
        //Reload List
        list.clear();
        Iterator it= s.iterator();
        while(it.hasNext()) {
            list.add((String)it.next());
        }

        //Save the updated Items to the Preferences
        editor.putStringSet(getString(R.string.befactor_preference_key),s);
        editor.commit();
        adapter.notifyDataSetChanged();
        // Clear focus from editText
        textfield.clearFocus();
        // Force hide of keyboard
        InputMethodManager myInputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        myInputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // Clear text in editText
        textfield.setText("");
        // Toast for user feedback
        Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.be_factor)+" " + activity + " "+getString(R.string.saved), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    /**
     * Reload the List on Start to display correct Values
     */
    @Override
    protected void onStart() {

        list.clear();
        Iterator it= s.iterator();
        while(it.hasNext()) {
            list.add((String)it.next());
        }

        editor.putStringSet(getString(R.string.befactor_preference_key),s);
        editor.commit();
        adapter.notifyDataSetChanged();

        super.onStart();
        // The activity is about to become visible.
    }

}
