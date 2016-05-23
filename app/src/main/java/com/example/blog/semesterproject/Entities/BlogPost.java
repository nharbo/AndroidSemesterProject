package com.example.blog.semesterproject.Entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by nicolaiharbo on 05/05/2016.
 */
public class BlogPost {

    public String title;
    public String content;
    public String author;
    public String coverpicString;
    public String latitude;
    public String longitude;

    public BlogPost(String author, String title, String content, String coverpicString) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.coverpicString = coverpicString;
    }

    public BlogPost(String author, String title, String content, String coverpicString, String latitude, String longitude) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.coverpicString = coverpicString;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverpicString() {
        return coverpicString;
    }

    public void setCoverpicString(String coverpicString) {
        this.coverpicString = coverpicString;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
