package com.example.noteit;

public class fireBaseModel {

    //upload and fetch variable should be same
    private String title;
    private String content;

    public fireBaseModel(){

    }

    public fireBaseModel(String title, String content){
        this.title = title;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
