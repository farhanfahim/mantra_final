package com.tekrevol.mantra.models.receiving_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.managers.FileManager;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.room_database_models.ListConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectCategoryConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectUserModelConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class MediaModel implements Serializable {


    transient boolean isChoice1;
    transient boolean isMedia1;

    @PrimaryKey(autoGenerate = true)
    private int dbId;

    private int currentUserId;

    @Expose
    @SerializedName("title")
    private String reminderText;

    private String fileLocalPath;
    @Expose
    @SerializedName("alarms")
    @TypeConverters(ListConverter.class)
    private List<AlarmModel> arrAlarm;
    private Date date;

    @Expose
    @SerializedName("icon_image_url")
    private String iconImageUrl;

    @Ignore
    @Expose
    @SerializedName("category")
    @TypeConverters(ObjectCategoryConverter.class)
    private Category category;

    @Ignore
    @Expose
    @SerializedName("user")
    @TypeConverters(ObjectUserModelConverter.class)
    private UserModel user;
    @Expose
    @SerializedName("original_playlist")
    private String originalPlaylist;
    @Expose
    @SerializedName("file_absolute_url")
    private String fileAbsoluteUrl;
    @Expose
    @SerializedName("image_url")
    private String imageUrl;
    @Expose
    @SerializedName("updated_at")
    private String updatedAt;
    @Expose
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    @SerializedName("total_favorite")
    private int totalFavorite;
    @Expose
    @SerializedName("total_share")
    private int totalShare;
    @Expose
    @SerializedName("total_like")
    private int totalLike;
    @Expose
    @SerializedName("file_url")
    private String fileUrl;
    @Expose
    @SerializedName("file_mime")
    private String fileMime;
    @Expose
    @SerializedName("file_type")
    private String fileType;
    @Expose
    @SerializedName("file_path")
    private String filePath;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("media_length")
    private int mediaLength;
    @Expose
    @SerializedName("category_id")
    private int categoryId;
    @Expose
    @SerializedName("user_id")
    private int userId;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private long id;
    @Expose
    @SerializedName("is_favourite")
    private int isFavourite;
    @Expose
    @SerializedName("is_like")
    private int isLike;



    public MediaModel(Parcel in) {
        reminderText = in.readString();
        fileLocalPath = in.readString();
        iconImageUrl = in.readString();
        originalPlaylist = in.readString();
        fileAbsoluteUrl = in.readString();
        imageUrl = in.readString();
        updatedAt = in.readString();
        createdAt = in.readString();
        totalFavorite = in.readInt();
        totalShare = in.readInt();
        totalLike = in.readInt();
        fileUrl = in.readString();
        fileMime = in.readString();
        fileType = in.readString();
        filePath = in.readString();
        image = in.readString();
        description = in.readString();
        mediaLength = in.readInt();
        categoryId = in.readInt();
        userId = in.readInt();
        name = in.readString();
        id = in.readLong();
        isFavourite = in.readInt();
        isLike = in.readInt();
    }

    public MediaModel() {

    }


    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

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

    public List<AlarmModel> getArrAlarm() {
        return arrAlarm;
    }

    public void setArrAlarm(List<AlarmModel> arrAlarm) {
        this.arrAlarm = arrAlarm;
    }

    public String getIconImageUrl() {
        return iconImageUrl;
    }

    public void setIconImageUrl(String iconImageUrl) {
        this.iconImageUrl = iconImageUrl;
    }

    public boolean isChoice1() {
        return isChoice1;
    }

    public void setChoice1(boolean choice1) {
        isChoice1 = choice1;
    }

    public boolean isMedia1() {
        return isMedia1;
    }


    public void setMedia1(boolean media1) {
        isMedia1 = media1;
    }


    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getOriginalPlaylist() {
        return originalPlaylist;
    }

    public void setOriginalPlaylist(String originalPlaylist) {
        this.originalPlaylist = originalPlaylist;
    }

    public String getFileAbsoluteUrl() {

        if (fileLocalPath != null && FileManager.isFileExits(fileLocalPath)) {
            return fileLocalPath;
        } else if (fileAbsoluteUrl != null) {
            return fileAbsoluteUrl;
        }
        return "";
    }

    public void setFileAbsoluteUrl(String fileAbsoluteUrl) {
        this.fileAbsoluteUrl = fileAbsoluteUrl;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getTotalFavorite() {
        return totalFavorite;
    }

    public void setTotalFavorite(int totalFavorite) {
        this.totalFavorite = totalFavorite;
    }

    public int getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(int totalShare) {
        this.totalShare = totalShare;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileMime() {
        return fileMime;
    }

    public void setFileMime(String fileMime) {
        this.fileMime = fileMime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMediaLength() {
        return mediaLength;
    }

    public void setMediaLength(int mediaLength) {
        this.mediaLength = mediaLength;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

}
