package com.softdesign.vkmusic.data.model;

import java.io.Serializable;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class Song {
    int id;
    String name;
    int albumId;
    String author;
    int duration;
    String url;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getAuthor() {
        return author;
    }

    public int getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbumId(int album) {
        this.albumId = album;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
