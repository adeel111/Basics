package com.example.adeeliftikhar.addnotes.DataProvider;

public class NotesDataProvider {

    private String noteTitle, noteDate, noteBody;

    public NotesDataProvider(String noteTitle, String noteDate, String noteBody) {
        this.setNoteTitle(noteTitle);
        this.setNoteDate(noteDate);
        this.setNoteBody(noteBody);
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }
}
