package com.tekrevol.mantra.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.models.receiving_model.Category;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.util.List;

public class ReminderMediaModel {

    @Expose
    @SerializedName("category")
    public Category category;
    @Expose
    @SerializedName("user")
    public UserModel user;
    @Expose
    @SerializedName("is_favourite")
    public int isFavourite;
    @Expose
    @SerializedName("is_like")
    public int isLike;
    @Expose
    @SerializedName("file_absolute_url")
    public String fileAbsoluteUrl;
    @Expose
    @SerializedName("image_url")
    public String imageUrl;
    @Expose
    @SerializedName("updated_at")
    public String updatedAt;
    @Expose
    @SerializedName("created_at")
    public String createdAt;
    @Expose
    @SerializedName("total_favorite")
    public int totalFavorite;
    @Expose
    @SerializedName("total_share")
    public int totalShare;
    @Expose
    @SerializedName("total_like")
    public int totalLike;
    @Expose
    @SerializedName("file_url")
    public String fileUrl;
    @Expose
    @SerializedName("file_mime")
    public String fileMime;
    @Expose
    @SerializedName("file_path")
    public String filePath;
    @Expose
    @SerializedName("description")
    public String description;
    @Expose
    @SerializedName("media_length")
    public int mediaLength;
    @Expose
    @SerializedName("type")
    public int type;
    @Expose
    @SerializedName("category_id")
    public int categoryId;
    @Expose
    @SerializedName("user_id")
    public int userId;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("id")
    public int id;

    public static class CategoryEntity {
        @Expose
        @SerializedName("children")
        public List<String> children;
        @Expose
        @SerializedName("children_count")
        public int childrenCount;
        @Expose
        @SerializedName("image_url")
        public String imageUrl;
        @Expose
        @SerializedName("icon_image_url")
        public String iconImageUrl;
        @Expose
        @SerializedName("type_text")
        public String typeText;
        @Expose
        @SerializedName("updated_at")
        public String updatedAt;
        @Expose
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("image")
        public String image;
        @Expose
        @SerializedName("icon_color")
        public String iconColor;
        @Expose
        @SerializedName("color")
        public String color;
        @Expose
        @SerializedName("icon")
        public String icon;
        @Expose
        @SerializedName("type")
        public int type;
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("parent_id")
        public int parentId;
        @Expose
        @SerializedName("id")
        public int id;
    }

    public static class UserEntity {
        @Expose
        @SerializedName("details")
        public DetailsEntity details;
        @Expose
        @SerializedName("created_at")
        public String createdAt;
        @Expose
        @SerializedName("email")
        public String email;
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("id")
        public int id;
    }

    public static class DetailsEntity {
        @Expose
        @SerializedName("full_name")
        public String fullName;
        @Expose
        @SerializedName("share_mantra_count")
        public int shareMantraCount;
        @Expose
        @SerializedName("my_mantra_count")
        public int myMantraCount;
        @Expose
        @SerializedName("media_count")
        public int mediaCount;
        @Expose
        @SerializedName("image_url")
        public String imageUrl;
        @Expose
        @SerializedName("about")
        public String about;
        @Expose
        @SerializedName("is_social_login")
        public int isSocialLogin;
        @Expose
        @SerializedName("email_updates")
        public int emailUpdates;
        @Expose
        @SerializedName("is_verified")
        public int isVerified;
        @Expose
        @SerializedName("image")
        public String image;
        @Expose
        @SerializedName("address")
        public String address;
        @Expose
        @SerializedName("phone")
        public String phone;
        @Expose
        @SerializedName("first_name")
        public String firstName;
        @Expose
        @SerializedName("id")
        public int id;
    }
}
