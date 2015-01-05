package com.example.fh.eddy;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 09.11.2014.
 * Edited by Tim Dez. 2014.
 */
public class FragmentTabOne extends ListFragment {

    DataHandler myDataHandler;

    MyArrayAdapter myDataAdapter;

    List<EntryData> entryDataList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.entry_tab, container, false);
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

        myDataAdapter= new MyArrayAdapter<EntryData>(getActivity(),entryDataList);
        setListAdapter(myDataAdapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            EntryData entryData = (EntryData) getListView().getItemAtPosition(position);

            showCancelDialog(entryData);

            return true;

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        EntryData entryData = (EntryData)l.getItemAtPosition(position);
        long theId = entryData.getId();

        EntryData usedEntry = myDataHandler.getSingleEntry(theId);

        Intent i = new Intent(getActivity().getBaseContext(), EditEntryForm.class);

        Bundle myBundle = new Bundle();
        myBundle.putSerializable("passedVar", usedEntry);
        i.putExtras(myBundle);

        startActivity(i);

    }



    public void showCancelDialog(final EntryData entryData)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.delete_Single_Entry_From_ListView));

        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                myDataHandler.deleteSingleEntry(entryData);
                myDataAdapter.remove(entryData);

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