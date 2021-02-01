package com.tekrevol.mantra.models.room_database_models;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tekrevol.mantra.models.receiving_model.Category;

import java.lang.reflect.Type;

public class ObjectCategoryConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static Category stringToSomeObject(String data) {
        if (data == null) {
            return null;
        }

        Type listType = new TypeToken<Category>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectToString(Category someObjects) {
        return gson.toJson(someObjects);
    }
}
