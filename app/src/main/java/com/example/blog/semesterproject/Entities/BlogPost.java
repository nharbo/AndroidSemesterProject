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
//    public ArrayList<Bitmap> images;

    public BlogPost(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
//        this.images = images;
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
//
//    public ArrayList<Bitmap> getImages() {
//        return images;
//    }
//
//    public void addImage(Bitmap image) {
//        images.add(image);
//    }
}
