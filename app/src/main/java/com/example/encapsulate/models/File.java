package com.example.encapsulate.models;

public class File {
    String fileID;
    String fileUrl;
    String caption;
    String fileUri;
    String fileType;

    public File(){}
    public File(String fileID, String fileUrl, String caption, String fileUri, String fileType) {
        this.fileID = fileID;
        this.fileUrl = fileUrl;
        this.caption = caption;
        this.fileUri = fileUri;
        this.fileType = fileType;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
