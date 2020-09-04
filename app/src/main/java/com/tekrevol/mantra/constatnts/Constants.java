package com.tekrevol.mantra.constatnts;

import com.tekrevol.mantra.models.DummyAdapterModel;
import com.tekrevol.mantra.models.SpinnerModel;

import java.util.ArrayList;

public class Constants {

    public static ArrayList<DummyAdapterModel> getDailyMantra() {
        ArrayList<DummyAdapterModel> arrayList = new ArrayList<>();
        arrayList.add(new DummyAdapterModel());
        arrayList.add(new DummyAdapterModel());
        arrayList.add(new DummyAdapterModel());

        return arrayList;
    }

    public static ArrayList<SpinnerModel> spinner(int count) {
        ArrayList<SpinnerModel> arrayList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            arrayList.add(new SpinnerModel("Item " + i));
        }

        return arrayList;
    }
}
