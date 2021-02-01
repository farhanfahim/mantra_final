package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.database.AlarmModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<AlarmModel> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<AlarmModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<AlarmModel> someObjects) {
        return gson.toJson(someObjects);
    }
}
