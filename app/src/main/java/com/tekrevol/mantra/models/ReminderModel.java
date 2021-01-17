package com.tekrevol.mantra.models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ReminderModel implements Serializable {

    @Expose
    @SerializedName("media_id")
    private long mediaId;
    @Expose
    @SerializedName("user_id")
    private int userId;
    @Expose
    @SerializedName("media")
    private ReminderMediaModel reminderMediaModel;
    @Expose
    @SerializedName("title")
    private String reminderText = "";
    private Date date;

    private ArrayList<AlarmModel> alarms = new ArrayList<>();


    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ReminderMediaModel getReminderMediaModel() {
        return reminderMediaModel;
    }

    public void setReminderMediaModel(ReminderMediaModel reminderMediaModel) {
        this.reminderMediaModel = reminderMediaModel;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<AlarmModel> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<AlarmModel> alarms) {
        this.alarms = alarms;
    }

    public long getMediaId() {
        return mediaId;
    }


    public void setArrAlarms(ArrayList<AlarmModel> arrAlarms) {
        this.alarms = arrAlarms;
    }

    public ArrayList<AlarmModel> getArrAlarms() {
        return alarms;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
