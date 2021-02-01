package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.SpinnerModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListSpinnerModelConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<SpinnerModel> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SpinnerModel>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<SpinnerModel> someObjects) {
        return gson.toJson(someObjects);
    }
}
