package com.example.ranobeparser.entity;

public class URLChapter {
    private String nameNovel;
    private String urlChapter;
    private String nameChapter;
    private boolean readChepter = false;
    private int positionRead;
    private String URLPage = "";

    public URLChapter(String urlChapter, String nameChapter) {
        this.urlChapter = urlChapter;
        this.nameChapter = nameChapter;
    }

    public String getUrlChapter() {
        return urlChapter;
    }

    public void setUrlChapter(String urlChapter) {
        this.urlChapter = urlChapter;
    }

    public String getNameChapter() {
        return nameChapter;
    }

    public void setNameChapter(String nameChapter) {
        this.nameChapter = nameChapter;
    }

    public boolean isReadChepter() {
        return readChepter;
    }

    public void setReadChepter(boolean readChepter) {
        this.readChepter = readChepter;
    }

    public int getPositionRead() {
        return positionRead;
    }

    public void setPositionRead(int positionRead) {
        this.positionRead = positionRead;
    }

    public String getNameNovel() {
        return nameNovel;
    }

    public void setNameNovel(String nameNovel) {
        this.nameNovel = nameNovel;
    }

    public String getURLPage() {
        return URLPage;
    }

    public void setURLPage(String URLPage) {
        this.URLPage = URLPage;
    }
}
