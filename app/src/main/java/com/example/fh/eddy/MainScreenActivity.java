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


public class MainScreenActivity extends Activity {

    ActionBar.Tab tab1, tab2, tab3;
    Fragment fragmentTab1 = new FragmentTabOne();
    //Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab2 = new GraphicFragment();
    Fragment fragmentTab3= new SettingsFragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            tab3.select();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, NewEntryForm.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
