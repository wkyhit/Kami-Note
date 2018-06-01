package com.example.zyq.kaminotetest.Class;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Label extends DataSupport {
    private String labelName;

    public List<MyNote> getNotes() {
        return notes;
    }

    private List<MyNote> notes = new ArrayList<>();

    /**
     * 添加传入的所有note到对应的label
     * @param notesToAdd    待添加的note列表
     */
    public void AddNotes(List<MyNote> notesToAdd) {
        notes.addAll(notesToAdd);
    }

    public void AddNote(MyNote noteToAdd) {
        notes.add(noteToAdd);
    }

    public void RemoveNotes(List<MyNote> notes) {
//        notes.
    }

    public Label(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
