package com.example.aydil.fragmenttofragmentcommunication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment {
    private TextView textViewForMessage;

    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        textViewForMessage = view.findViewById(R.id.text_view_for_message);
        Bundle bundle = getArguments();
        String message = bundle.getString("message");
        textViewForMessage.setText(message);
        return view;

    }

}
