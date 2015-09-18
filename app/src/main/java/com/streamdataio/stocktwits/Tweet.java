package com.streamdataio.stocktwits;

import java.io.Serializable;

public class Tweet implements Serializable{
    private String body;
    private String dateTime;
    private String imgURL;
    private User user;

    public Tweet(String aBody, String aDateTime, String anImgUrl, User aUser){
        this.body = aBody;
        this.dateTime = aDateTime;
        this.imgURL = anImgUrl;
        this.user = aUser;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}