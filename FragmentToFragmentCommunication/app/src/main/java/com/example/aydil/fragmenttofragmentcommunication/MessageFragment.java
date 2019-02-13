package com.example.aydil.fragmenttofragmentcommunication;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class MessageFragment extends Fragment {

    private EditText editTextMessage;
    private Button buttonSendMessage;

    OnMessageSendListener messageSendListener;

    public interface OnMessageSendListener
    {
        public void onMessageSend(String message);
    }

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        editTextMessage = view.findViewById(R.id.edit_text_message);
        buttonSendMessage = view.findViewById(R.id.button_send_message);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                messageSendListener.onMessageSend(message);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try
        {
            messageSendListener = (OnMessageSendListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "Must Implement onMessageMethod");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editTextMessage.setText("");
    }
}
