package com.tekrevol.mantra.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;

public class UserAction {
    @Expose
    @SerializedName("action_type")
    private int actionType;
    @Expose
    @SerializedName("media_id")
    private long mediaId;

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
