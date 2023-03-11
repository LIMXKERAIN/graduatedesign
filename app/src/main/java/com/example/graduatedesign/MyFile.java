package com.example.graduatedesign;

public class MyFile {

    private String fileName;

    private String fileGenerateTime;

    public MyFile(String fileName, String fileGenerateTime) {
        this.fileName = fileName;
        this.fileGenerateTime = fileGenerateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileGenerateTime() {
        return fileGenerateTime;
    }

    public void setFileGenerateTime(String fileGenerateTime) {
        this.fileGenerateTime = fileGenerateTime;
    }
}
