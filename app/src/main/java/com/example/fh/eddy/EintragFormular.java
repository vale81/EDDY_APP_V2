package com.example.fh.eddy;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Fabian on 09.11.2014.
 */
public class EintragFormular extends Activity{

    private EditText usersBz;
    private Button save_Button;
    private TextView bzdata;


    // Um Datenbank öffnen zu können brauchen wir ein DataHandler Objekt
    DataHandler the_handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eintrag_formular);
        // The activity is being created.

        //init Users BZ
        usersBz = (EditText) findViewById(R.id.users_Bz);
        // init Save Button
        save_Button = (Button) findViewById(R.id.save_button);
        // INIt test textview clickable
        bzdata = (TextView) findViewById(R.id.bzbzbz);



        bzdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String derWert = " ";
                // Handler objekt anlegen
                the_handler = new DataHandler(getBaseContext());
                the_handler.open();
                Cursor c = the_handler.returnData();
                // Falls Daten vorhanden setze BZwert auf String an Index stelle 0
                // Index startet bei 0, jeder Eintrag (Bolus Basis etc) an entsprechender Stelle
                // muss noch erweitert werden soll nur grundfunktion testen
                if(c.moveToFirst())
                {
                    derWert = c.getString(0);
                }
                bzdata.setText("BZ:" + derWert);
                the_handler.closeDatabase();
                Toast.makeText(getBaseContext(), "BZ WERT:" + derWert, Toast.LENGTH_LONG).show();

            }
        });

        save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getBZWert = usersBz.getText().toString();
                the_handler = new DataHandler(getBaseContext());
                the_handler.open();
                the_handler.insertData(getBZWert, null, null, null, null, null, null);
                Toast.makeText(getBaseContext(), "Data inserted", Toast.LENGTH_LONG).show();
                the_handler.closeDatabase();
            }
        });
    }

    // Test BZ Übergabe NUR UM DATENBANK zu TESTEN später löschen!!!!!







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
