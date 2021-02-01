package com.tekrevol.mantra.roomdatabase.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tekrevol.mantra.models.receiving_model.MediaModel;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM mediamodel WHERE currentUserId = :userId")
    List<MediaModel> getCurrentUserMantra(int userId);

    @Insert
    void insert(MediaModel mediaModel);

    @Delete
    void deleteMedia(MediaModel mediaModel);

    @Update
    void updateMedia(MediaModel mediaModel);



}