package com.example.dylanrodbar.reproductor;

/**
 * Created by dylanrodbar on 17/3/2018.
 */

public class LRCSong {

    private String content;
    private long duration;
    private long sleepDuration;

    LRCSong(String content, long duration) {
        this.content = content;
        this.duration = duration;
    }

    public String getContent() {
        return this.content;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getSleepDuration() {
        return sleepDuration;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSleepDuration(long sleepDuration) {
        this.sleepDuration = sleepDuration;
    }
}
