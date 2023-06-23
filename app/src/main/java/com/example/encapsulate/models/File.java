package com.example.encapsulate.models;

public class File {
    String fileUrl;
    String caption;

    String fileType;

    public File(){}
    public File(String fileUrl, String caption, String fileType) {

        this.fileUrl = fileUrl;
        this.caption = caption;
        this.fileType = fileType;
    }


    public String getFileUrl() {
        return fileUrl;
    }


    public String getCaption() {
        return caption;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
