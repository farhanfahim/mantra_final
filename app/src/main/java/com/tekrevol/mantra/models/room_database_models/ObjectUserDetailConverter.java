package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.UserDetails;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.lang.reflect.Type;

public class ObjectUserDetailConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static UserDetails stringToSomeObject(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<UserDetails>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectToString(UserDetails someObjects) {
        return gson.toJson(someObjects);
    }
}
