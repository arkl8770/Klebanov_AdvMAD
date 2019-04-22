package com.example.albumlist_realm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private AlbumAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Album objects
        RealmResults<Album> albums = realm.where(Album.class).findAll();

        albumAdapter = new AlbumAdapter(albums, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        //assign the adapter to the recyclerView
        recyclerView.setAdapter(albumAdapter);

        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText albumEditText = new EditText(MainActivity.this);
                albumEditText.setHint("Album Name");
                layout.addView(albumEditText);

                final EditText artistEditText = new EditText(MainActivity.this);
                artistEditText.setHint("Artist Name");
                layout.addView(artistEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Add Album");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get album name entered
                        final String newAlbumName = albumEditText.getText().toString();
                        final String newArtistName = artistEditText.getText().toString();

                        if (!newAlbumName.isEmpty()) {
                            addAlbum(UUID.randomUUID().toString(), newAlbumName, newArtistName);
                        }
                    }
                });

                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
    }

    public void addAlbum(final String newId, final String newAlbumName, final String newArtistName) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //create a realm object
                Album newAlbum = realm.createObject(Album.class, newId);
                newAlbum.setAlbumName(newAlbumName);
                newAlbum.setArtistName(newArtistName);
            }
        });
    }

    public void changeAlbumListened(final String albumId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Album album = realm.where(Album.class).equalTo("id", albumId).findFirst();
                album.setListened(!album.getListened());
            }
        });
    }

    private void changeAlbum(final String albumId, final String albumName, final String artistName) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Album album = realm.where(Album.class).equalTo("id", albumId).findFirst();
                album.setAlbumName(albumName);
                album.setArtistName(artistName);
            }
        });
    }

    public void editAlbum(final String albumId) {

        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Album album = realm.where(Album.class).equalTo("id", albumId).findFirst();

        //create edit texts and add to layout
        final EditText albumEditText = new EditText(MainActivity.this);
        albumEditText.setText(album.getAlbumName());
        layout.addView(albumEditText);
        final EditText artistEditText = new EditText(MainActivity.this);
        artistEditText.setText(album.getArtistName());
        layout.addView(artistEditText);

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Edit Album");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save edited album
                //get updated album and artist name
                final String updatedAlbumName = albumEditText.getText().toString();
                final String updatedArtistName = artistEditText.getText().toString();

                if(!updatedAlbumName.isEmpty()) {
                    changeAlbum(albumId, updatedAlbumName, updatedArtistName);
                }
            }
        });

        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //delete album
                deleteAlbum(albumId);
            }
        });

        dialog.create();
        dialog.show();
    }

    private void deleteAlbum(final String albumId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Album.class).equalTo("id", albumId)
                        .findFirst()
                        .deleteFromRealm();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //close the Realm instance when the Activity exists
        realm.close();
    }
}
