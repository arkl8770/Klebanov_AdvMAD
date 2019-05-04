package com.example.final_ariklebanov;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get realm instance
        realm = Realm.getDefaultInstance();
        //get all saved Restaurant objects
        RealmResults<Restaurant> restaurants = realm.where(Restaurant.class).findAll();
        restaurantAdapter = new RestaurantAdapter(restaurants, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //assign the adapter to the recyclerView
        recyclerView.setAdapter(restaurantAdapter);
        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                //create edit texts and add to layout
                final EditText nameEditText = new EditText(MainActivity.this);
                nameEditText.setHint("Restaurant name");
                layout.addView(nameEditText);
                final EditText urlEditText = new EditText(MainActivity.this);
                urlEditText.setHint("Restaurant url");
                layout.addView(urlEditText);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Add Restaurant");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get restaurant name entered
                        final String newRestaurantName = nameEditText.getText().toString();
                        final String newRestaurantUrl = urlEditText.getText().toString();
                        if (!newRestaurantName.isEmpty()) {
                            addRestaurant(UUID.randomUUID().toString(), newRestaurantName, newRestaurantUrl);
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });
    }

    private void changeRestaurant(final String restaurantId, final String restaurantName, final String restaurantUrl) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Restaurant restaurant = realm.where(Restaurant.class).equalTo("id", restaurantId).findFirst();
                restaurant.setName(restaurantName);
                restaurant.setUrl(restaurantUrl);
            }
        });
    }

    public void onListItemClick(String restaurantId) {

        Intent intent = new Intent(MainActivity.this, RestaurantDetail.class);
        intent.putExtra("url", realm.where(Restaurant.class).equalTo("id", restaurantId).findFirst().getUrl());
        startActivity(intent);
    }

    public void editRestaurant(final String restaurantId) {
        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Restaurant restaurant = realm.where(Restaurant.class).equalTo("id", restaurantId).findFirst();

        final EditText nameEditText = new EditText(MainActivity.this);
        nameEditText.setText(restaurant.getName());
        layout.addView(nameEditText);
        final EditText urlEditText = new EditText(MainActivity.this);
        urlEditText.setText(restaurant.getUrl());
        layout.addView(urlEditText);

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Edit Restaurant");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //save edited restaurant
                //get updated restaurant and url
                final String updatedName = nameEditText.getText().toString();
                final String updatedUrl = urlEditText.getText().toString();
                if(!updatedName.isEmpty()) {
                    changeRestaurant(restaurantId,updatedName, updatedUrl);
                }
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete restaurant
                deleteRestaurant(restaurantId);
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //close the Realm instance when the activity exits
        realm.close();
    }

    private void deleteRestaurant(final String restaurantId) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Restaurant.class).equalTo("id", restaurantId).findFirst().deleteFromRealm();
            }
        });
    }

    public void addRestaurant(final String newId, final String newName, final String newUrl) {
        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //create a realm object
                Restaurant newRestaurant = realm.createObject(Restaurant.class, newId);
                newRestaurant.setName(newName);
                newRestaurant.setUrl(newUrl);
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
}
