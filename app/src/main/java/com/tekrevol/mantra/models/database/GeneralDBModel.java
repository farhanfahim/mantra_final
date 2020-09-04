package com.tekrevol.mantra.models.database;

import com.tekrevol.mantra.enums.DBModelTypes;
import com.tekrevol.mantra.managers.retrofit.GsonFactory;
import com.tekrevol.mantra.models.receiving_model.MediaModel;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

// GeneralDBModel.java
@Entity
public class GeneralDBModel {

    // Same id as child model
    @Id
    public long id;
    public String jsonData;
    public int dbModelTypes;
    public Date createdAt;
    public Date date;



    public GeneralDBModel() {
    }

    /**
     * @param id
     * @param jsonData
     */
    public GeneralDBModel(long id, String jsonData, DBModelTypes dbModelTypes) {
        this.id = id;
        this.jsonData = jsonData;
        this.dbModelTypes = dbModelTypes.ordinal();
        this.createdAt = new Date();
        this.date = new Date();
    }


    public MediaModel getMedia() {
//        DBModelTypes value = DBModelTypes.values()[dbModelTypes];
        if (dbModelTypes == DBModelTypes.SCHEDULED_MANTRA.ordinal()) {
            return GsonFactory.getSimpleGson().fromJson(jsonData, MediaModel.class);
        }

        return null;
    }

}
