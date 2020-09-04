package com.tekrevol.mantra.models;

public class DummyAdapterModel {
    String text1;
    String text2;
    String text3;
    int id;
    int resId1;
    int resId2;
    String url;
    transient boolean isChoice1;

    public boolean isChoice1() {
        return isChoice1;
    }

    public void setChoice1(boolean choice1) {
        isChoice1 = choice1;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResId1() {
        return resId1;
    }

    public void setResId1(int resId1) {
        this.resId1 = resId1;
    }

    public int getResId2() {
        return resId2;
    }

    public void setResId2(int resId2) {
        this.resId2 = resId2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
