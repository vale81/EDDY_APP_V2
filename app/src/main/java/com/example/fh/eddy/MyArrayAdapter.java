package com.example.fh.eddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Array Adapter Class for the Entry List
 * Set the Values for every Entry in the List in the RowLayout
 * @author Fabian
 */
public class MyArrayAdapter<E> extends ArrayAdapter<EntryData> {
    private final Context context;
    private List<EntryData> values= new ArrayList<EntryData>();

    public MyArrayAdapter(Context context, List<EntryData> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    /**
     * Edit the current Row according to the Entry
     * Set Values of the Layout with the Values of the current Entry
     * @param position Current Entry of the List
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Assign the view we are converting to a local variable
        View rowView = convertView;

        //First check to see if the view is null. if so, we have to inflate(render) it.
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.rowlayout, null);
        }


        //Get the current Entry of the List, specified by position
        EntryData i = values.get(position);

        if (i != null) {

            //Obtain the reference for the several Views
            ImageView colorView = (ImageView) rowView.findViewById(R.id.color);
            TextView bloodView = (TextView) rowView.findViewById(R.id.blood);
            TextView dateView = (TextView) rowView.findViewById(R.id.date);
            ImageView activityView = (ImageView) rowView.findViewById(R.id.activity);
            ImageView eventView = (ImageView) rowView.findViewById(R.id.event);

            //Retrieve the lower and upper blood sugar bounds (needed to Calculate the appropriate Color Image)
            int lower_bound;
            int upper_bound;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            try {
                lower_bound = Integer.parseInt(sharedPrefs.getString(getContext().getString(R.string.untere_blutzuckergrenze_preference_key), "75"));
                upper_bound = Integer.parseInt(sharedPrefs.getString(getContext().getString(R.string.obere_blutzuckergrenze_preference_key), "200"));
            } catch(Exception e) {
                lower_bound=75;
                upper_bound=200;
            }

            //Set the "traffic light" picture according to the BloodSugar Value of the Entry and the Upper/Lower Bounds
            if(i.getBloodSugarValue() < lower_bound) {
                colorView.setImageResource(R.drawable.red_picture);
            } else if(i.getBloodSugarValue() >= lower_bound && i.getBloodSugarValue() <=120) {
                colorView.setImageResource(R.drawable.green_picture);
            } else if(i.getBloodSugarValue() > 120 && i.getBloodSugarValue() <= upper_bound) {
                colorView.setImageResource(R.drawable.yellow_picture);
            } else if(i.getBloodSugarValue() >= upper_bound) {
                colorView.setImageResource(R.drawable.red_picture);
            }

            //Set the Blood Sugar Value
            bloodView.setText(i.getBloodSugarValue()+"");

            //Set the Date/Time
            dateView.setText(i.getTheDate()+" "+i.getDaytime());

            //Show the Activity Picture if the Entry has a Activity
            //If not show an Transparent(Invisible) Picture
            if(!(i.getActivity().startsWith("Keine Aktivität"))) {

                activityView.setImageResource(R.drawable.activity_picture);
                activityView.setAlpha(255F);
            } else {
                activityView.setImageResource(R.drawable.black_picture);
                activityView.setAlpha(0F);
            }

            //Show the Event Picture if the Entry has a Event
            //If not show an Transparent(Invisible) Picture
            if(!(i.getEvent().startsWith("Kein Ereignis"))) {

                eventView.setImageResource(R.drawable.event_picture);
                eventView.setAlpha(255F);
            } else {
                eventView.setImageResource(R.drawable.black_picture);
                eventView.setAlpha(0F);
            }
        }

        // the view must be returned to our activity
        return rowView;
    }
}
