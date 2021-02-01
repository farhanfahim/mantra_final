package com.tekrevol.mantra.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by hamza.ahmed on 3/16/2018.
 */
@Entity
public class SpinnerModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int dbId;

    private String text;
    private boolean isSelected = false;
    private int positionInList = 0;


    public int getPositionInList() {
        return positionInList;
    }

    public void setPositionInList(int positionInList) {
        this.positionInList = positionInList;
    }

    public SpinnerModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpinnerModel) {
            return ((SpinnerModel) obj).text.equals(text);
        }
        return super.equals(obj);
    }
}
