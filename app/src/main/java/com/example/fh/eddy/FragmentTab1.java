package com.example.fh.eddy;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Fabian on 09.11.2014.
 */
public class FragmentTab1 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.eintrag_tab, container, false);
        TextView textview = (TextView) view.findViewById(R.id.txtOne);
        textview.setText(R.string.Eintrag);
        return view;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(getActivity(), EintragFormular.class);
        startActivity(intent);
    }
}
