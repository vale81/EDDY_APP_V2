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
        //final View view = inflater.inflate(R.layout.grafik_tab, container, false);
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

        myDataHandler = new DataHandler(getActivity().getBaseContext());
        myDataHandler.open();
        //final List<EintragDaten> eintragDatenListe = new ArrayList<>(myDataHandler.getEveryEntryUnsorted());
        long now = new Date().getTime();
        final List<EintragDaten> eintragDatenListe = new ArrayList<>(myDataHandler.getEntryUntil(now-(dateoffset)));
        myDataHandler.closeDatabase();
        int num = eintragDatenListe.size();

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[num];
        for(int i=0;i<num;i++) {
            data[i]=new GraphView.GraphViewData(eintragDatenListe.get(i).getUnix_time(),eintragDatenListe.get(i).getBloodSugarValue());
        }
        GraphViewSeries exampleSeries = new GraphViewSeries(data);

        // init example series data
        /*long now = new Date().getTime();
        GraphViewSeries exampleSeries = new GraphViewSeries(new GraphView.GraphViewData[] {
                new GraphView.GraphViewData(now+(1*60*60*24*1000), 60)
                , new GraphView.GraphViewData(now+(2*60*60*24*1000), 70)
                , new GraphView.GraphViewData(now+(3*60*60*24*1000), 90)
                , new GraphView.GraphViewData(now+(4*60*60*24*1000), 80)
                , new GraphView.GraphViewData(now+(5*60*60*24*1000), 80)
                , new GraphView.GraphViewData(now+(6*60*60*24*1000), 80)
                , new GraphView.GraphViewData(now+(7*60*60*24*1000), 40)
                , new GraphView.GraphViewData(now+(8*60*60*24*1000), 80)
                , new GraphView.GraphViewData(now+(9*60*60*24*1000), 90)
                , new GraphView.GraphViewData(now+(10*60*60*24*1000), 120)
                , new GraphView.GraphViewData(now+(11*60*60*24*1000), 60)
                , new GraphView.GraphViewData(now+(12*60*60*24*1000), 65)

        });*/

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
                    //return null;
                    //String stringvalue=value+"";
                    //String stringvalue=String.valueOf(value);
                    //return stringvalue;
                    //return "#";
                } else {
                    return null;
                }
                //return null; // let graphview generate Y-axis label for us
            }
        });
        //graphView.setHorizontalLabels(new String[] {"20.12.2014", "21.12.2014","22.12.2014","23.12.2014","24.12.2014","25.12.2014","26.12.2014","27.12.2014","28.12.2014","29.12.2014","30.12.2014","31.12.2014"});

        graphView.addSeries(exampleSeries); // data
        graphView.setDrawDataPoints(true);
        // set view port, start=2, size=40
        //graphView.setViewPort(0, 1);

        // optional - activate scaling / zooming
        graphView.setScalable(true);
        graphView.setScrollable(true);

        //Set y axis values (max,min) (am besten vorher schauen was min max werte sind, statt preferences)
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        graphView.setManualYAxisBounds(Integer.parseInt(sharedPrefs.getString("obere_blutzuckergrenze","200")),Integer.parseInt(sharedPrefs.getString("untere_blutzuckergrenze","0")));


        try {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.graph1);
            layout.removeAllViews();
            layout.addView(graphView);
        } catch (NullPointerException e) {
            // something to handle the NPE.
        }
    }



}

