package com.example.fitnesstrackerlite;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Food extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    private String name;
    private Integer calories;
    private Long dateEatenMilli;
    private Date timeEaten;

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

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Long getDateEatenMilli() {
        return dateEatenMilli;
    }

    public void setDateEatenMilli(Long dateEatenMilli) {
        this.dateEatenMilli = dateEatenMilli;
    }

    public Date getTimeEaten() {
        return timeEaten;
    }

    public void setTimeEaten(Date timeEaten) {
        this.timeEaten = timeEaten;
    }
}
