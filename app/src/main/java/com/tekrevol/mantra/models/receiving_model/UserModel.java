package com.tekrevol.mantra.models.receiving_model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.models.SpinnerModel;
import com.tekrevol.mantra.models.UserDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.mantra.models.room_database_models.ListConverter;
import com.tekrevol.mantra.models.room_database_models.ListSpinnerModelConverter;
import com.tekrevol.mantra.models.room_database_models.ListUserModelConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectCategoryConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectUserDetailConverter;

import java.io.Serializable;
import java.util.List;
@Entity
public class UserModel implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int dbId;

    @Expose
    @SerializedName("expires_in")
    private int expiresIn;
    @Expose
    @SerializedName("token_type")
    private String tokenType;
    @Expose
    @SerializedName("access_token")
    private String accessToken;
    @Expose
    @SerializedName("specialization")

    @TypeConverters(ListSpinnerModelConverter.class)
    private List<SpinnerModel> specializations;
    @Expose
    @SerializedName("dependants")
    @TypeConverters(ListUserModelConverter.class)
    private List<UserModel> dependants;
    @Expose
    @SerializedName("details")

    @TypeConverters(ObjectUserDetailConverter.class)
    private UserDetails userDetails;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("chat_enabled")
    private boolean chatEnabled;
    @Expose
    @SerializedName("review_enabled")
    private boolean reviewEnabled;
    @Expose
    @SerializedName("roles_csv")
    private String roles_csv;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;

    transient boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

   /* public int getRoles_csv() {
        if (roles_csv == null || roles_csv.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(roles_csv);
    }

    public void setRoles_csv(String roles_csv) {
        this.roles_csv = roles_csv;
    }
*/

    public String getRoles_csv() {
        return roles_csv;
    }

    public void setRoles_csv(String roles_csv) {
        this.roles_csv = roles_csv;
    }

    public boolean getChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }

    public boolean getReviewEnabled() {
        return reviewEnabled;
    }

    public void setReviewEnabled(boolean reviewEnabled) {
        this.reviewEnabled = reviewEnabled;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<SpinnerModel> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpinnerModel> specializations) {
        this.specializations = specializations;
    }

    public List<UserModel> getDependants() {
        return dependants;
    }

    public void setDependants(List<UserModel> dependants) {
        this.dependants = dependants;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }


}