package com.example.fh.eddy;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
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
        // Database operation -- only open Database once
        // Kernel closes automatically
        myDataHandler = new DataHandler(getActivity().getBaseContext());
        myDataHandler.open();
        // Getting all entries in database
        entryDataList = myDataHandler.getEveryEntry();

        //entryDataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, entryDataList);
        myDataAdapter= new MyArrayAdapter<EintragDaten>(getActivity(),entryDataList);
        setListAdapter(myDataAdapter);
        //setListAdapter(entryDataAdapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            EintragDaten eintragDaten = (EintragDaten) getListView().getItemAtPosition(position);

            showCancelDialog(eintragDaten);

            return true;

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
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



    public void showCancelDialog(final EintragDaten eintragDaten)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.delete_Single_Entry_From_ListView));

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                myDataHandler.deleteSingleEntry(eintragDaten);
                myDataAdapter.remove(eintragDaten);

                Toast toast = Toast.makeText(getActivity().getBaseContext(), getString(R.string.delete_Single_Entry_Toast), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Simply dismiss the dialog on negative user choice
                dialog.dismiss();
            }
        });

        builder.show();
    } //End showCancelDialog

} // End ListFragment
