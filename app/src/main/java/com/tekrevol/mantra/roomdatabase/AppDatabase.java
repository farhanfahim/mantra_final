package com.tekrevol.mantra.roomdatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tekrevol.mantra.models.SpinnerModel;
import com.tekrevol.mantra.models.UserDetails;
import com.tekrevol.mantra.models.database.AlarmModel;
import com.tekrevol.mantra.models.receiving_model.MediaModel;
import com.tekrevol.mantra.models.receiving_model.UserModel;
import com.tekrevol.mantra.models.room_database_models.DateConverter;
import com.tekrevol.mantra.models.room_database_models.ListConverter;
import com.tekrevol.mantra.models.room_database_models.ListSpinnerModelConverter;
import com.tekrevol.mantra.models.room_database_models.ListUserModelConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectCategoryConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectUserDetailConverter;
import com.tekrevol.mantra.models.room_database_models.ObjectUserModelConverter;
import com.tekrevol.mantra.roomdatabase.interfaces.MediaDao;

@Database(entities = {AlarmModel.class, MediaModel.class, UserModel.class, UserDetails.class, SpinnerModel.class}, version = 1)
@TypeConverters({ListConverter.class, DateConverter.class, ObjectCategoryConverter.class, ObjectUserModelConverter.class,
        ObjectUserDetailConverter.class, ListUserModelConverter.class, ListSpinnerModelConverter.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract MediaDao mediaDao();
}
