package com.tekrevol.mantra.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "media.db";
    private static final int SCHEMA = 1;

    private static final String TABLE_NAME = "media";

    public static final String _ID = "_id";
    public static final String IS_CHOICE_1 = "isChoice1";
    public static final String IS_MEDIA_1 = "isMedia1";
    public static final String FILE_LOCAL_PATH = "fileLocalPath";
    public static final String ICON_IMAGE_URL = "iconImageUrl";
    public static final String ORIGINAL_PLAYLIST = "originalPlaylist";
    public static final String COL_MANTRA_TITLE = "mantra_title";
    public static final String COL_MANTRA_DESCRIPTION = "mantra_description";
    public static final String COL_MANTRA_REMINDER_TITLE = "mantra_reminder_title";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";

    private static DatabaseHelper sInstance = null;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.i(getClass().getSimpleName(), "Creating database...");

        final String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_ALARMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new UnsupportedOperationException("This shouldn't happen yet!");
    }


}
