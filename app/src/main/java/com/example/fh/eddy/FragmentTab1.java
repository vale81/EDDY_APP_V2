package com.example.fh.eddy;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 09.11.2014.
 * Edited by Tim Dez. 2014.
 */
public class FragmentTab1 extends ListFragment {

    DataHandler myDataHandler;

    ArrayAdapter<EintragDaten> entryDataAdapter;
    MyArrayAdapter myDataAdapter;

    List<EintragDaten> entryDataList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.eintrag_tab, container, false);
        return view;
    }

    @Override
  public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        myDataHandler = new DataHandler(getActivity().getBaseContext());
        myDataHandler.open();

        entryDataList = myDataHandler.getEveryEntry();

        //entryDataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, entryDataList);
        myDataAdapter = new MyArrayAdapter(getActivity(),entryDataList);
        setListAdapter(myDataAdapter);
        //setListAdapter(entryDataAdapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EintragDaten eintragDaten = (EintragDaten) getListView().getItemAtPosition(position);


                myDataHandler.deleteSingleEntry(eintragDaten);
                //entryDataAdapter.remove(eintragDaten);
                myDataAdapter.remove(eintragDaten);
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
       // myDataHandler = new DataHandler(getActivity().getBaseContext());
       // myDataHandler.open();
        EintragDaten eintragDaten = (EintragDaten)l.getItemAtPosition(position);
        long theId = eintragDaten.getId();

        EintragDaten usedEntry = myDataHandler.getSingleEntry(theId);
        //super.onListItemClick(l, v, position, id);

        Intent i = new Intent(getActivity().getBaseContext(), EditEntryForm.class);

        Bundle myBundle = new Bundle();
        myBundle.putSerializable("passedVar", usedEntry);
        i.putExtras(myBundle);

        startActivity(i);
      //  myDataHandler.closeDatabase();
        Toast.makeText(getActivity().getBaseContext(),
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        myDataHandler.closeDatabase();
    }
} // End ListFragment
