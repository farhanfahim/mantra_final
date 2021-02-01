package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.lang.reflect.Type;

public class ObjectUserModelConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static UserModel stringToSomeObject(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<UserModel>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectToString(UserModel someObjects) {
        return gson.toJson(someObjects);
    }
}
