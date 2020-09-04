package com.tekrevol.mantra.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;

public class FeedbackModel {
    @Expose
    @SerializedName("feedback")
    public String feedback;
    @Expose
    @SerializedName("expectation_level")
    public int expectation_level;
    @Expose
    @SerializedName("user_id")
    public int user_id;
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getExpectation_level() {
        return expectation_level;
    }

    public void setExpectation_level(int expectation_level) {
        this.expectation_level = expectation_level;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
