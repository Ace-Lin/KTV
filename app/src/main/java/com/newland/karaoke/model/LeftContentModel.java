package com.newland.karaoke.model;

public class LeftContentModel {
    private int imageId;
    private String text;
    private int id;

    public LeftContentModel(int imageId, String text, int id) {
        this.imageId = imageId;
        this.text = text;
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
