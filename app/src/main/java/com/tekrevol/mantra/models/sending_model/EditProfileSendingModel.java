package com.tekrevol.mantra.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;

public class EditProfileSendingModel {


    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("about")
    private String about;
    @Expose
    @SerializedName("name")
    private String name;

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
