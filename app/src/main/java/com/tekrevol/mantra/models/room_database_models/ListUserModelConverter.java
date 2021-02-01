package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListUserModelConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<UserModel> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<UserModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<UserModel> someObjects) {
        return gson.toJson(someObjects);
    }
}
