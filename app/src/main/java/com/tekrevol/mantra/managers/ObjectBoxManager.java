package com.tekrevol.mantra.managers;


import android.content.Context;

import com.tekrevol.mantra.BaseApplication;
import com.tekrevol.mantra.enums.DBModelTypes;
import com.tekrevol.mantra.models.database.GeneralDBModel;
import com.tekrevol.mantra.models.database.GeneralDBModel_;
import com.tekrevol.mantra.models.receiving_model.MediaModel;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class ObjectBoxManager {
    public static ObjectBoxManager INSTANCE = null;
    private BaseApplication app;


    public BoxStore getBoxStore() {
        return app.getBoxStore();
    }


    // ****** DECLARE ROWS ***********//


    public Box<GeneralDBModel> getGeneralDBBox() {


        return getBoxStore().boxFor(GeneralDBModel.class);

    }

    // ******** GET QUERIES *************** //


    // Return specific media model
    public MediaModel getScheduledMantraMediaModel(long id) {
        GeneralDBModel generalDBModel = getGeneralDBBox().query().equal(GeneralDBModel_.id, id).build().findFirst();
        if (generalDBModel != null) {
            return generalDBModel.getMedia();
        }

        return null;
    }

    /*public List<GeneralDBModel> queryForSpecificMedia(int currentUserId) {
        String userId = String.valueOf(currentUserId);
        List<GeneralDBModel> generalDBModel = getGeneralDBBox().query()
                .contains(GeneralDBModel_.currentUserId, userId)
                .build()
                .find();

        return  generalDBModel;
    }*/

    // Return all media models for Scheduled Mantra
    public ArrayList<MediaModel> getAllScheduledMantraMediaModels(Context context) {
        //List<GeneralDBModel> all = getGeneralDBBox().query().order(GeneralDBModel_.date, QueryBuilder.DESCENDING).build().find();
        List<GeneralDBModel> all = getGeneralDBBox().query().order(GeneralDBModel_.id,QueryBuilder.DESCENDING).build().find();
       // List<GeneralDBModel> all = getGeneralDBBox().getAll();

        ArrayList<MediaModel> mantrasToDelete = new ArrayList<>();
        ArrayList<MediaModel> mediaModels = new ArrayList<>();



        for (GeneralDBModel generalDBModel : all) {
            if (generalDBModel.getMedia().getAlarms().isEmpty()) {
                mantrasToDelete.add(generalDBModel.getMedia());
            } else {
                int currentUserId = SharedPreferenceManager.getInstance(context).getCurrentUser().getId();
                if (generalDBModel.currentUserId == currentUserId){
                    mediaModels.add(generalDBModel.getMedia());
                }

            }
        }


        for (MediaModel mediaModel : mantrasToDelete) {
            removeGeneralDBModel(mediaModel.getId());
        }


        return mediaModels;
    }

    public ArrayList<Long> test(Context context) {
        //List<GeneralDBModel> all = getGeneralDBBox().query().order(GeneralDBModel_.date, QueryBuilder.DESCENDING).build().find();
        List<GeneralDBModel> all = getGeneralDBBox().query().order(GeneralDBModel_.id,QueryBuilder.DESCENDING).build().find();
        // List<GeneralDBModel> all = getGeneralDBBox().getAll();

        ArrayList<MediaModel> mantrasToDelete = new ArrayList<>();
        ArrayList<Long> mediaModels = new ArrayList<>();



        for (GeneralDBModel generalDBModel : all) {
            if (generalDBModel.getMedia().getAlarms().isEmpty()) {
                mantrasToDelete.add(generalDBModel.getMedia());
            } else {
                int currentUserId = SharedPreferenceManager.getInstance(context).getCurrentUser().getId();
                if (generalDBModel.currentUserId == currentUserId){
                    mediaModels.add(generalDBModel.id);
                }

            }
        }


        return mediaModels;
    }

    public List<GeneralDBModel> getAllScheduledMantraMediaModelsTest() {
        List<GeneralDBModel> all = getGeneralDBBox().query().order(GeneralDBModel_.id).build().find();
        // List<GeneralDBModel> all = getGeneralDBBox().getAll();


        return all;
    }


    // ***************** PUT OPERATIONS *******************//


    public long putGeneralDBModel(long id,int currentUserId, String jsonString, DBModelTypes dbModelTypes) {
        GeneralDBModel generalDBModel = new GeneralDBModel(id, currentUserId, jsonString, dbModelTypes);
        return getGeneralDBBox().put(generalDBModel);
    }


    // *************** DELETE OPERATIONS *****************//


    public void removeGeneralDBModel(long id) {
        getGeneralDBBox().remove(id);
    }

    public void removeAllDB() {
        getGeneralDBBox().removeAll();
    }


    private ObjectBoxManager(BaseApplication app) {
        this.app = app;
    }


    public static ObjectBoxManager getInstance(BaseApplication app) {
        if (INSTANCE == null)
            INSTANCE = new ObjectBoxManager(app);

        return INSTANCE;
    }


}
