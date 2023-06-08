package com.android.demo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class RecordParam implements Serializable {

    public static final int DEFAULT_MIN_DURATION = 10;
    public static final int DEFAULT_MAX_DURATION = 60;
    private String videoPath;
    private String videoScreenPath;
    private String videoUrl;
    private long size;
    private int duration;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoScreenPath() {
        return videoScreenPath;
    }

    public void setVideoScreenPath(String videoScreenPath) {
        this.videoScreenPath = videoScreenPath;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String toJSONString(){
        return JSON.toJSONString(this);
    }
}
