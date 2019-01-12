package com.example.adeeliftikhar.addnotes.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.addnotes.EditNoteActivity;
import com.example.adeeliftikhar.addnotes.MainActivity;
import com.example.adeeliftikhar.addnotes.R;
import com.example.adeeliftikhar.addnotes.SQLiteDatabase.DatabaseOperations;

import java.util.ArrayList;

public class NotesRVAdapter extends RecyclerView.Adapter<NotesRVAdapter.NotesViewHolder> {

    private Context context;
    private Cursor myCursor;
    private static SQLiteDatabase sqLiteDB;
    private DatabaseOperations databaseOperations;

    private ArrayList<String> myListData = new ArrayList<>();

    public NotesRVAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.myCursor = cursor;
        myCursor.moveToFirst();
        while (!myCursor.isAfterLast()) {
            myListData.add(myCursor.getString(myCursor.getColumnIndex("id")));
            myCursor.moveToNext();
        }
        databaseOperations = new DatabaseOperations(context);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notes_rv_design, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, int position) {
        if (!myCursor.moveToPosition(holder.getAdapterPosition())) {
            return;
        }
        String title = myCursor.getString(myCursor.getColumnIndex("title"));
        String date = myCursor.getString(myCursor.getColumnIndex("date"));
        String notes = myCursor.getString(myCursor.getColumnIndex("notes_content"));

        holder.textViewTitle.setText(title);
        holder.textViewDate.setText(date);
        holder.textViewNotes.setText(notes);

        holder.imageButtonDeleteNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemIdFromDB = Integer.parseInt(myListData.get(holder.getAdapterPosition()));
                int viewPosition = holder.getAdapterPosition();
//                Toast.makeText(context, "position => " + itemIdFromDB, Toast.LENGTH_SHORT).show();
                deleteAlertDialogBox(viewPosition, itemIdFromDB);
            }
        });
        holder.imageButtonEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = Integer.parseInt(myListData.get(holder.getAdapterPosition()));
//                Toast.makeText(context, "position => " + itemPosition, Toast.LENGTH_SHORT).show();
                sqLiteDB = databaseOperations.getReadableDatabase();
                showSpecificNoteFromDb(itemPosition);
            }
        });
    }

    private void deleteAlertDialogBox(final int viewPosition, final int itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqLiteDB = databaseOperations.getWritableDatabase();
                databaseOperations.deleteNote(itemId, sqLiteDB);
//                Toast.makeText(context, "New Position => " + itemId, Toast.LENGTH_SHORT).show();
                Cursor myCursor = MainActivity.getAllItems();
                swapCursor(myCursor);
                remove(viewPosition);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void showSpecificNoteFromDb(int itemPosition) {
        sqLiteDB = databaseOperations.getReadableDatabase();
        Cursor cursor = databaseOperations.showSpecificNote(itemPosition, sqLiteDB);
        if (cursor == null) {
            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(0);
                    String title = cursor.getString(1);
                    String date = cursor.getString(2);
                    String note = cursor.getString(3);
                    Intent myIntent2 = new Intent(context, EditNoteActivity.class);
                    myIntent2.putExtra("id", id);
                    myIntent2.putExtra("title", title);
                    myIntent2.putExtra("date", date);
                    myIntent2.putExtra("noteContent", note);
                    context.startActivity(myIntent2);
                } while (cursor.moveToNext());
            }
        }
    }

    public void swapCursor(Cursor newCursor) {
        if (myCursor != null) {
            myCursor.close();
        }
        myCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    private void remove(int position) {
        myListData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, myListData.size());
        notifyDataSetChanged();
//        Toast.makeText(context, "Item Deleted From ViewHolder", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return myCursor.getCount();
    }

    //    NotesViewHolder Class...
    class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate, textViewNotes;
        ImageButton imageButtonEditNotes, imageButtonDeleteNotes;

        NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewNotes = itemView.findViewById(R.id.text_view_notes);
            imageButtonEditNotes = itemView.findViewById(R.id.image_button_edit_notes);
            imageButtonDeleteNotes = itemView.findViewById(R.id.image_button_delete_notes);
        }
    }

//    End of NotesViewHolder Class.
}