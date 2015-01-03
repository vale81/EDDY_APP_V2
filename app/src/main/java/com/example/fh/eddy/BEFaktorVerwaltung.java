package com.example.fh.eddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import java.util.List;
import java.util.Set;

/**
 * Created by Fabian on 21.11.2014.
 */
public class BEFaktorVerwaltung extends Activity {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    Set<String> s;
    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verwaltungs_layout);
        TextView txtview = (TextView) findViewById(R.id.textview_verwaltung);
        txtview.setText(R.string.be_verwaltung_text);
        Button button=(Button)findViewById(R.id.new_Item);
        button.setText(R.string.be_verwaltung_button);
        EditText txtfield=(EditText) findViewById(R.id.textfield_verwaltung);
        txtfield.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        s = new HashSet<String>(sharedPrefs.getStringSet("be_factor", new HashSet<String>()));
        listview = (ListView) findViewById(R.id.listView);
        list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                R.layout.aktivitaeten_listview_layout, R.id.firstLine, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                showDialog((String)parent.getItemAtPosition(position));
            }
        });

    }

    public void showDialog(final String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_Be) +" "+ text);

        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                list.remove(text);
                s.remove(text);
                editor.putStringSet("be_factor",s);
                editor.commit();
                adapter.notifyDataSetChanged();
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

    public void saveItem(View view) {

        EditText textfield= (EditText)findViewById(R.id.textfield_verwaltung);
        String activity=textfield.getText().toString();

        s.add(activity);

        list.clear();
        Iterator it= s.iterator();
        while(it.hasNext()) {
            list.add((String)it.next());
        }

        editor.putStringSet("be_factor",s);
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

    @Override
    protected void onStart() {

        list.clear();
        Iterator it= s.iterator();
        while(it.hasNext()) {
            list.add((String)it.next());
        }

        editor.putStringSet("be_factor",s);
        editor.commit();
        adapter.notifyDataSetChanged();

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
