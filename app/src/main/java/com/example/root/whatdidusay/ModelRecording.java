package com.example.root.whatdidusay;

/**
 * Created by dottechnologies on 1/9/15.
 * this class is models for recording item
 */
public class ModelRecording {

    private String id;
    private String name;
    private String date;
    private String time;
    private String duration;
    private String path;
    private Boolean isPlaying;
    private Boolean isForDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean isPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Boolean isForDelete() {
        return isForDelete;
    }

    public void setIsForDelete(Boolean isForDelete) {
        this.isForDelete = isForDelete;
    }
}
