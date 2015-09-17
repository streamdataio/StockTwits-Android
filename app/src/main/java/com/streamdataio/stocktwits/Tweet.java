package com.streamdataio.stocktwits;

import java.io.Serializable;

public class Tweet implements Serializable{
    public String body;
    public String dateTime;
    public String imgURL;
    public User user;

    public Tweet(String body_, String date_time_, String imgUrl_, User u){
        this.body = body_;
        this.dateTime = date_time_; // time is GMT+0
        this.imgURL = imgUrl_;
        this.user = u;
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