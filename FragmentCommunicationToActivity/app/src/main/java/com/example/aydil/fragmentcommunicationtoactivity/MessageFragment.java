package com.example.aydil.fragmentcommunicationtoactivity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private EditText editTextMessage;
    private Button sendButton;
    //    Call back for interface or Reference of Interface.
    OnMessageReadListener messageReadListener;

    public MessageFragment() {
        // Required empty public constructor
    }

    public interface OnMessageReadListener {
        public void onMessageRead(String message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        editTextMessage = view.findViewById(R.id.edit_text_message);
        sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                messageReadListener.onMessageRead(message);
            }
        });
        return view;
    }
//    This method will check either the interface is implemented by the parent or not.

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        try {
            messageReadListener = (OnMessageReadListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "Must Override onMessageReadMethod.");
        }
    }
}
