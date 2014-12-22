package com.example.fh.eddy;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 09.11.2014.
 * Edited by Tim Dez. 2014.
 */
public class FragmentTab1 extends ListFragment {

    DataHandler myDataHandler;
    ListView myListView;

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
        List<EintragDaten> entryDataList = new ArrayList<>(myDataHandler.getEveryEntry());

        ArrayAdapter<EintragDaten> entryDataAdapter = new ArrayAdapter<EintragDaten>(
                getActivity(), android.R.layout.simple_list_item_1, entryDataList);
        setListAdapter(entryDataAdapter);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(getActivity(), EintragFormular.class);
        startActivity(intent);
    }
}
