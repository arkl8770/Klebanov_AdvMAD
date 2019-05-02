package com.example.fitnesstrackerlite;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Drink extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    private String name;
    private Integer volume;
    private Long dateDrankMilli;
    private Date timeDrank;

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

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Long getDateDrankMilli() {
        return dateDrankMilli;
    }

    public void setDateDrankMilli(Long dateDrankMilli) {
        this.dateDrankMilli = dateDrankMilli;
    }

    public Date getTimeDrank() {
        return timeDrank;
    }

    public void setTimeDrank(Date timeDrank) {
        this.timeDrank = timeDrank;
    }
}
