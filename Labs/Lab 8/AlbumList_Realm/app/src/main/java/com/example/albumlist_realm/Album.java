package com.example.albumlist_realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Album extends RealmObject {

    @Required
    @PrimaryKey
    private String id;
    private String albumName;
    private String artistName;
    private boolean listened;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName=albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName=artistName;
    }

    public boolean getListened() {
        return listened;
    }

    public void setListened(boolean listened) {
        this.listened=listened;
    }
}
