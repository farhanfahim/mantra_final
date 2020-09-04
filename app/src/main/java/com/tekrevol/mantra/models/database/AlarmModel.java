package com.tekrevol.mantra.models.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;

import java.io.Serializable;

public class AlarmModel implements Serializable {

    @Expose
    @SerializedName("alarm_id")
    private int alarmId;
    @Expose
    @SerializedName("date_TIME")
    private String dateTime;
    @Expose
    @SerializedName("unixDTTM")
    private long unixDTTM;

    public AlarmModel(Parcel in) {
        alarmId = in.readInt();
        dateTime = in.readString();
        unixDTTM = in.readLong();
    }



    public AlarmModel() {

    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getUnixDTTM() {
        return unixDTTM;
    }

    public void setUnixDTTM(long unixDTTM) {
        this.unixDTTM = unixDTTM;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
