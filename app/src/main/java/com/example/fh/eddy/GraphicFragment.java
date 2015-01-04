package com.example.fh.eddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    String head="";
    View view;
    DataHandler myDataHandler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.grafik_tab, container, false);
        final Button button1 = (Button) view.findViewById(R.id.one_week);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.week);
                custom_populateGraphView(view,"E H:m",7*60*60*24*1000);
            }
        });

        final Button button2 = (Button) view.findViewById(R.id.one_month);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.month);
                long offset=31*(long)(60*60*24*1000);
                custom_populateGraphView(view,"d. H 'Uhr'",offset);
            }
        });

        final Button button3 = (Button) view.findViewById(R.id.three_months);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                head=getString(R.string.months);
                long offset=3*31*(long)(60*60*24*1000);
                custom_populateGraphView(view,"MMM d",offset);
            }
        });


        head=getString(R.string.week);
        custom_populateGraphView(view,"E H:m",7*60*60*24*1000);

        return view;
    }

    private void custom_populateGraphView(View view,String date_Format,long dateoffset) {

        int maxvalue=0;
        int minvalue=999;
        myDataHandler = new DataHandler(getActivity().getBaseContext());
        myDataHandler.open();

        long now = new Date().getTime();
        final List<EintragDaten> eintragDatenListe = new ArrayList<>(myDataHandler.getEntryUntil(now-(dateoffset)));
        myDataHandler.closeDatabase();
        int num = eintragDatenListe.size();

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[num];
        for(int i=0;i<num;i++) {
            data[i]=new GraphView.GraphViewData(eintragDatenListe.get(i).getUnix_time(),eintragDatenListe.get(i).getBloodSugarValue());
            if(eintragDatenListe.get(i).getBloodSugarValue()>maxvalue) {
                maxvalue=eintragDatenListe.get(i).getBloodSugarValue();
            }
            if(eintragDatenListe.get(i).getBloodSugarValue()<minvalue) {
                minvalue=eintragDatenListe.get(i).getBloodSugarValue();
            }

        }
        GraphViewSeries exampleSeries = new GraphViewSeries(data);


        LineGraphView graphView = new LineGraphView(
                getActivity() // context
                , head // heading
        );

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

        graphView.addSeries(exampleSeries); // data
        graphView.setDrawDataPoints(true);


        // optional - activate scaling / zooming
        graphView.setScalable(true);
        graphView.setScrollable(true);

        //Set y axis values (max,min)
        graphView.setManualYAxisBounds(maxvalue,minvalue);


        try {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(graphView);
        } catch (NullPointerException e) {
            // something to handle the NPE.
        }
    }
}

