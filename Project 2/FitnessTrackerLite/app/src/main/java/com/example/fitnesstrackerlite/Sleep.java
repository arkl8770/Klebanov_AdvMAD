package com.example.fitnesstrackerlite;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Sleep extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    private Integer rating;
    private Date timeStarted;
    private Date timeEnded;
    private Long dateSleptMilli;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Date timeEnded) {
        this.timeEnded = timeEnded;
    }

    public Long getDateSleptMilli() {
        return dateSleptMilli;
    }

    public void setDateSleptMilli(Long dateSleptMilli) {
        this.dateSleptMilli = dateSleptMilli;
    }
}
