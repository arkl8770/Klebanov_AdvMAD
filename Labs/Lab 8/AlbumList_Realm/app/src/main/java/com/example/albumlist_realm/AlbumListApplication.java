package com.example.albumlist_realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

public class AlbumListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Realm
        Realm.init(this);

        //define the configuration for Realm
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();

        //IF YOU CHANGE CLASS STRUCTURE, UNCOMMENT FOLLOWING LINE, RUN, AND COMMENT IT OUT AGAIN
        //Realm.deleteRealm(realmConfig);

        //set the default realm configuration
        Realm.setDefaultConfiguration(realmConfig);
    }
}
