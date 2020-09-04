package com.tekrevol.mantra.models.sending_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;

public class UploadMedia {

    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("media_length")
    private String mediaLength;
    @Expose
    @SerializedName("type")
    private int fileType;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("category_id")
    private int categoryId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaLength() {
        return mediaLength;
    }

    public void setMediaLength(String mediaLength) {
        this.mediaLength = mediaLength;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
