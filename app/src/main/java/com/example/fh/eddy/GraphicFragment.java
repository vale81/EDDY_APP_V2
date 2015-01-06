package com.example.fh.eddy;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Fabian on 09.11.2014.
 */
public class GraphicFragment extends PreferenceFragment {

    private String head="";
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.graphic_tab, container, false);

        //Button for 1 Week
        final Button button1 = (Button) view.findViewById(R.id.one_week);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.week);
                custom_populateGraphView(view,"E H:m",7*60*60*24*1000);
            }
        });
        //Button for 1 Month
        final Button button2 = (Button) view.findViewById(R.id.one_month);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.month);
                long offset=31*(long)(60*60*24*1000);
                custom_populateGraphView(view,"d. H 'Uhr'",offset);
            }
        });
        //Button for 3 Months
        final Button button3 = (Button) view.findViewById(R.id.three_months);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.months);
                long offset=3*31*(long)(60*60*24*1000);
                custom_populateGraphView(view,"MMM d",offset);
            }
        });

        //As default display 1 Week
        head=getString(R.string.week);
        custom_populateGraphView(view,"E H:m",7*60*60*24*1000);

        return view;
    }

    /**
     * Populates the Graph View with Data
     * Retrieves needed Data and specifies Graph Values
     *
     * @param date_Format Format of the Date on X Axis
     * @param dateoffset specifies the range from Now till Now-dateoffset
     */
    private void custom_populateGraphView(View view,String date_Format,long dateoffset) {

        int maxvalue=0;
        int minvalue=999;
        DataHandler myDataHandler = new DataHandler(getActivity().getBaseContext());
        myDataHandler.open();

        long now = new Date().getTime();
        final List<EntryData> entryDataListe = new ArrayList<>(myDataHandler.getEntryUntil(now - (dateoffset)));
        myDataHandler.closeDatabase();
        int num = entryDataListe.size();

        //Populates the GraphView Data and searches Min/Max Values
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[num];
        for(int i=0;i<num;i++) {
            data[i]=new GraphView.GraphViewData(entryDataListe.get(i).getUnix_time(), entryDataListe.get(i).getBloodSugarValue());
            if(entryDataListe.get(i).getBloodSugarValue()>maxvalue) {
                maxvalue= entryDataListe.get(i).getBloodSugarValue();
            }
            if(entryDataListe.get(i).getBloodSugarValue()<minvalue) {
                minvalue= entryDataListe.get(i).getBloodSugarValue();
            }

        }
        GraphViewSeries exampleSeries = new GraphViewSeries(data);


        LineGraphView graphView = new LineGraphView(
                getActivity() // context
                , head // heading
        );

        //Sets the X Axis of the Graph in the specified Date Format
        final SimpleDateFormat dateFormat = new SimpleDateFormat(date_Format);
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) value);
                    return dateFormat.format(d);

                } else {
                    return null;
                }

            }
        });

        //Adds Data
        graphView.addSeries(exampleSeries);
        //Enables the drawing of Data Points
        graphView.setDrawDataPoints(true);

        //optional - activate scaling / zooming
        graphView.setScalable(true);
        graphView.setScrollable(true);

        //Set y axis values (max,min)
        graphView.setManualYAxisBounds(maxvalue,minvalue);


        try {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph1);
            layout.removeAllViews();
            //If not enough Entries exist, the Graph can not be displayed
            if(num<2) {
                //Show Message to the User that not enough Entries exist to display the Graph
                Toast toast=Toast.makeText(getActivity(), getString(R.string.not_enough_entries), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            } else {
                layout.addView(graphView);
            }
        } catch (NullPointerException e) {
            // something to handle the NPE.
        }
    }
}

