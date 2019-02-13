package com.example.aydil.customdialogbox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.ETC1;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogBoxActivity extends AppCompatDialogFragment {
    private EditText editTextUserName;
    private EditText editTextUserPass;
    private DialogBoxActivityListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Creates a builder for an alert dialog that uses the default alert dialog theme.
//        If you are going to display the alert dialog in the Activity use Activity.this ,
//        if it is in Fragment then use getActivity() during initialization of AlertDialog.Builder(*).
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.activity_dialog_box_layout, null);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_home);
        builder.setTitle("Login");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userName = editTextUserName.getText().toString();
                String userPassword = editTextUserPass.getText().toString();
                listener.applyTexts(userName, userPassword);
            }
        });
        editTextUserName = view.findViewById(R.id.edit_text_user_name);
        editTextUserPass = view.findViewById(R.id.edit_text_user_pass);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
//        onAttach(Activity) called once the fragment is associated with its activity. (int position,
//        long id) { showDetails(position); }, Helper function to show the details of a selected item,
//        Return the Context this fragment is currently associated with.
        super.onAttach(context);
//        CTRL + ALT + T ==> To implement try catch block...
        try {
            listener = (DialogBoxActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must Implement DialogBoxActivityListener in MainActivity");
        }
    }

    public interface DialogBoxActivityListener {
        void applyTexts(String userName, String userPassword);
    }
}
