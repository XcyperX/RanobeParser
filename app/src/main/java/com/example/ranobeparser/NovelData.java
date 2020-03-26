package com.example.ranobeparser;

import java.util.ArrayList;
import java.util.HashMap;

public class NovelData {
    private String nameNovel;
    private String infoNovel;
    private String photoId;
    private String urlNovel;
    private HashMap<String, String> urlChepterNovel;

    public NovelData(String nameNovel, String photoId, String urlNovel) {
        this.nameNovel = nameNovel;
        this.photoId = photoId;
        this.urlNovel = urlNovel;
    }

    public String getNameNovel() {
        return nameNovel;
    }

    public void setNameNovel(String nameNovel) {
        this.nameNovel = nameNovel;
    }

    public String getInfoNovel() {
        return infoNovel;
    }

    public void setInfoNovel(String infoNovel) {
        this.infoNovel = infoNovel;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUrlNovel() {
        return urlNovel;
    }

    public void setUrlNovel(String urlNovel) {
        this.urlNovel = urlNovel;
    }

    public HashMap<String, String> getUrlChepterNovel() {
        return urlChepterNovel;
    }

    public void setUrlChepterNovel(HashMap<String, String> urlChepterNovel) {
        this.urlChepterNovel = urlChepterNovel;
    }
}
