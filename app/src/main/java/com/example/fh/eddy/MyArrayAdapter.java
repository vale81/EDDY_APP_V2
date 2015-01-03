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
 * Created by Fabian on 03.01.2015.
 */
public class MyArrayAdapter<E> extends ArrayAdapter<EintragDaten> {
    private final Context context;
    private List<EintragDaten> values= new ArrayList<EintragDaten>();

    public MyArrayAdapter(Context context, List<EintragDaten> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View rowView = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.rowlayout, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        EintragDaten i = values.get(position);
        int size=values.size();

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            ImageView colorView = (ImageView) rowView.findViewById(R.id.color);
            TextView bloodView = (TextView) rowView.findViewById(R.id.blood);
            TextView dateView = (TextView) rowView.findViewById(R.id.date);
            ImageView activityView = (ImageView) rowView.findViewById(R.id.activity);
            ImageView eventView = (ImageView) rowView.findViewById(R.id.event);

            // check to see if each individual textview is null.
            // if not, assign some text!

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            int lower_bound=Integer.parseInt(sharedPrefs.getString("untere_blutzuckergrenze", "40"));
            int upper_bound=Integer.parseInt(sharedPrefs.getString("obere_blutzuckergrenze", "170"));

            if(i.getBloodSugarValue() < lower_bound) {
                colorView.setImageResource(R.drawable.red_picture);
            } else if(i.getBloodSugarValue() >= lower_bound && i.getBloodSugarValue() <= upper_bound-(2*(upper_bound/3))) {
                colorView.setImageResource(R.drawable.yellow_picture);
            } else if(i.getBloodSugarValue() >= lower_bound && i.getBloodSugarValue() <= upper_bound) {
                colorView.setImageResource(R.drawable.green_picture);
            } else if(i.getBloodSugarValue() > upper_bound) {
                colorView.setImageResource(R.drawable.red_picture);
            }

            bloodView.setText(i.getBloodSugarValue()+"");

            dateView.setText(i.getTheDate()+" "+i.getDaytime());

            if(i.getActivity().equals("Keine Aktivität")) {

                activityView.setImageResource(android.R.color.transparent);
            }
            if(i.getEvent().equals("Kein Ereignis")) {

                eventView.setImageResource(android.R.color.transparent);
            }
        }

        // the view must be returned to our activity
        return rowView;


        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        ImageView colorView = (ImageView) rowView.findViewById(R.id.color);
        TextView bloodView = (TextView) rowView.findViewById(R.id.blood);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);
        ImageView activityView = (ImageView) rowView.findViewById(R.id.activity);
        ImageView eventView = (ImageView) rowView.findViewById(R.id.event);

        bloodView.setText("Test");
        bloodView.setText(values.get(position).getBloodSugarValue());


        return rowView;*/
    }
}