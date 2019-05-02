package com.example.fitnesstrackerlite;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Exercise extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    private String name;
    private Integer duration;
    private Long dateStartedMilli;
    private Date timeStarted;

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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getDateStartedMilli() {
        return dateStartedMilli;
    }

    public void setDateStartedMilli(Long dateStartedMilli) {
        this.dateStartedMilli = dateStartedMilli;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }
}
