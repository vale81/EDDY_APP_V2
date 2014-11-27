package com.example.fh.eddy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
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
public class AktivitaetenVerwaltung extends Activity {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    Set<String> s;
    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;
    //StableArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verwaltungs_layout);
        TextView txtview = (TextView) findViewById(R.id.textview_verwaltung);
        txtview.setText("Neue Aktivität angeben");
        Button button=(Button)findViewById(R.id.new_Item);
        button.setText("Aktivität speichern");

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPrefs.edit();

        s = new HashSet<String>(sharedPrefs.getStringSet("activities", new HashSet<String>()));
        listview = (ListView) findViewById(R.id.listView);
        list = new ArrayList<String>();

        //Iterator it= s.iterator();
        //while(it.hasNext()) {
        //    list.add((String)it.next());
        //}

        //adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        adapter = new ArrayAdapter<String>(this,
                R.layout.aktivitaeten_listview_layout, R.id.firstLine, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String text= (String)parent.getItemAtPosition(position);
                list.remove(text);
                s.remove(text);
                editor.putStringSet("activities",s);
                editor.commit();
                adapter.notifyDataSetChanged();
                Toast toast=Toast.makeText(getApplicationContext(),"Aktivität "+text+" gelöscht", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
        });

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
        }
        /*HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }*/

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

        editor.putStringSet("activities",s);
        editor.commit();
        adapter.notifyDataSetChanged();
        Toast toast=Toast.makeText(getApplicationContext(), "Aktivität " + activity + " gespeichert", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();

        //AlertDialog.Builder alert=new AlertDialog.Builder(this);
        //alert.setTitle("Aktivität gespeichert").setMessage("Aktivität "+activity+" gespeichert").show();
    }

    @Override
    protected void onStart() {

        list.clear();
        Iterator it= s.iterator();
        while(it.hasNext()) {
            list.add((String)it.next());
        }

        editor.putStringSet("activities",s);
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
