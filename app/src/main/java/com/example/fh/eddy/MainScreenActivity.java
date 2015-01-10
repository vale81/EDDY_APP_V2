package com.example.fh.eddy;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;
import java.util.Random;

/**
 * Main Activity
 * Create the Fragments and populate the Action Bar Tabs
 * @author Fabian Tim
 */
public class MainScreenActivity extends Activity {

    private ActionBar.Tab tab1;
    private ActionBar.Tab tab2;
    private ActionBar.Tab tab3;
    private Fragment fragmentTab1 = new FragmentTabOne();
    private Fragment fragmentTab2 = new GraphicFragment();
    private Fragment fragmentTab3= new SettingsFragment();

    /**
     * Initialise the Tab Fragments and add them to the Action Bar,
     * load the default Values for Preferences
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //Initialise Default Values of Preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        tab1 = actionBar.newTab().setText(R.string.Eintrag);
        tab2 = actionBar.newTab().setText(R.string.Grafik);
        tab3 = actionBar.newTab().setText(R.string.Einstellungen);

        tab1.setTabListener(new MyTabListener(fragmentTab1));
        tab2.setTabListener(new MyTabListener(fragmentTab2));
        tab3.setTabListener(new MyTabListener(fragmentTab3));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);

     //   exampleDataCreation();
    }

    /**
     * Initialize the Menu
     * Load Layout for the Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    /**
     * Handle Selection of Menu Items
     * Switches to Settings Fragment if Settings was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Select the Settings Fragment if Settings was pressed
        if (id == R.id.action_settings) {
            tab3.select();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle the on Click Event of the Button,
     * and Start the NewEntryForm Activity
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, NewEntryForm.class);
        startActivity(intent);
    }

    /**
     * Creates Sample Data
     * DO NOT USE IN FINAL VERSION
     */
    private void exampleDataCreation(){

        long now=new Date().getTime();

        DataHandler myDataHandler;
        myDataHandler = new DataHandler(this);
        myDataHandler.open();
        Random r = new Random();

        int quantity=75;
        for(int i=20;i<quantity;i++) {

            int bloodSugarValue= r.nextInt(i*3+10);
            String j=String.valueOf(i);
            long timestamp=now-i*(long)(60*60*24*1000);
            myDataHandler.insertNewData(bloodSugarValue,j,j,j,j,j,j,j,timestamp);
        }

        myDataHandler.closeDatabase();
    }

}
