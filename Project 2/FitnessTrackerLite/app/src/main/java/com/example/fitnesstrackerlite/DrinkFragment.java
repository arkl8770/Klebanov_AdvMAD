package com.example.fitnesstrackerlite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkFragment extends Fragment {

    private Realm realm;
    private DrinkAdapter drinkAdapter;

    private Integer totalVolume;

    private TextView drinkHeaderText;

    public DrinkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Drink objects
        RealmResults<Drink> drinks = realm.where(Drink.class).findAll();
        //drinks = drinks.sort("timeDrank", Sort.DESCENDING);

        //calculates totalVolume upon fragment load
        totalVolume = 0;
        for (int i=0; i < drinks.size(); i++) {
            totalVolume = totalVolume + drinks.get(i).getVolume();
        }

        drinkAdapter = new DrinkAdapter(drinks, this);

        View rootView = inflater.inflate(R.layout.fragment_drink, container, false);

        drinkHeaderText = rootView.findViewById(R.id.drinkHeaderText);
        drinkHeaderText.setText("You've drank " + totalVolume.toString() + " oz of fluids so far today!");

        RecyclerView recyclerView = rootView.findViewById(R.id.drinkRecyclerView);

        //assign the adapter to the recyclerView
        recyclerView.setAdapter(drinkAdapter);

        //set the layout manager to the recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create a vertical linear layout to hold edit texts
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);

                //create edit texts and add to layout
                final EditText nameEditText = new EditText(getActivity());
                nameEditText.setHint("Drink name");
                layout.addView(nameEditText);
                final EditText volumeEditText = new EditText(getActivity());
                volumeEditText.setHint("Volume (oz)");
                volumeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(volumeEditText);
                final TimePicker timePicker = new TimePicker(getActivity());
                layout.addView(timePicker);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Add Drink");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get drink name entered as String
                        final String newDrinkName = nameEditText.getText().toString();

                        //get volume entered as Integer
                        final Integer newVolumeCount;
                        if (volumeEditText.getText().toString().isEmpty()) {
                            newVolumeCount = 0;
                        } else {
                            newVolumeCount = Integer.parseInt(volumeEditText.getText().toString());
                        }

                        //get timeDrank as Date
                        Date newTimeDrank = Calendar.getInstance().getTime();;
                        final Integer timeHour = timePicker.getCurrentHour();
                        final Integer timeMinute = timePicker.getCurrentMinute();
                        final String timeString = timeHour + ":" + timeMinute;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        try {
                            newTimeDrank = simpleDateFormat.parse(timeString);
                            Log.e("Time", newTimeDrank.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //get dateEaten in milliseconds
                        Date currentDate = new Date();
                        final long newDateDrankMilli = currentDate.getTime();

                        //addFood using above variables
                        if (!newDrinkName.isEmpty()) {
                            Log.e("Almost", "Attempt to add drink entry");
                            addDrink(UUID.randomUUID().toString(), newDrinkName, newVolumeCount, newTimeDrank, newDateDrankMilli);
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //close the Realm instance when the Activity exits
        realm.close();
    }

    public void deleteDrink(final String drinkId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Drink.class).equalTo("id", drinkId).findFirst().deleteFromRealm();
            }
        });
    }

    public void changeDrink(final String drinkId, final String drinkName, final Integer volumeCount, final Date timeDrank) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Drink drink = realm.where(Drink.class).equalTo("id", drinkId).findFirst();
                drink.setName(drinkName);
                drink.setVolume(volumeCount);
                drink.setTimeDrank(timeDrank);
            }
        });

        Drink drink = realm.where(Drink.class).equalTo("id", drinkId).findFirst();
        Integer volumeDiff = volumeCount - drink.getVolume();
        totalVolume = calculateTotalVolume(volumeDiff);
        drinkHeaderText.setText("You've drank " + totalVolume.toString() + " oz of fluids so far today!");
    }

    public void editDrink(final String drinkId) {

        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        Drink drink = realm.where(Drink.class).equalTo("id", drinkId).findFirst();

        //create edit texts and add to layout
        final EditText nameEditText = new EditText(getActivity());
        nameEditText.setText(drink.getName());
        nameEditText.setHint("Drink Name");
        layout.addView(nameEditText);
        final EditText volumeEditText = new EditText(getActivity());
        volumeEditText.setText(drink.getVolume().toString());
        volumeEditText.setHint("Volume (oz)");
        volumeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(volumeEditText);
        final TimePicker timePicker = new TimePicker(getActivity());
        timePicker.setCurrentHour(drink.getTimeDrank().getHours());
        timePicker.setCurrentMinute(drink.getTimeDrank().getMinutes());
        layout.addView(timePicker);

        //create alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Edit Drink");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save edited Drink
                //get updated Drink and its variables

                //get updatedDrinkName entered as String
                final String updatedDrinkName = nameEditText.getText().toString();

                //get updated volume entered as Integer
                final Integer updatedVolumeCount;
                if (volumeEditText.getText().toString().isEmpty()) {
                    updatedVolumeCount = 0;
                } else {
                    updatedVolumeCount = Integer.parseInt(volumeEditText.getText().toString());
                }

                //get updatedTimeDrank as Date
                Date updatedTimeDrank = Calendar.getInstance().getTime();;
                final Integer timeHour = timePicker.getCurrentHour();
                final Integer timeMinute = timePicker.getCurrentMinute();
                final String timeString = timeHour + ":" + timeMinute;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    updatedTimeDrank = simpleDateFormat.parse(timeString);
                    Log.e("Time", updatedTimeDrank.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //editDrink using above variables
                if (!updatedDrinkName.isEmpty()) {
                    Log.e("Almost", "Attempt to edit drink entry");
                    changeDrink(drinkId, updatedDrinkName, updatedVolumeCount, updatedTimeDrank);
                }
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete Drink
                deleteDrink(drinkId);
                totalVolume = calculateTotalVolume(-1*(realm.where(Drink.class).equalTo("id", drinkId).findFirst().getVolume()));
                drinkHeaderText.setText("You've drank " + totalVolume.toString() + " oz of fluids so far today!");
            }
        });
        dialog.show();
    }

    public void addDrink(final String newId, final String newDrinkName, final Integer newVolumeCount, final Date newTimeDrank, final long newDateDrankMilli) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //create a new realm Drink object
                Drink newDrink = realm.createObject(Drink.class, newId);
                newDrink.setName(newDrinkName);
                newDrink.setVolume(newVolumeCount);
                newDrink.setTimeDrank(newTimeDrank);
                newDrink.setDateDrankMilli(newDateDrankMilli);
                Log.e("newDrink", newDrinkName);
                Log.e("newVolume", newVolumeCount.toString());
                Log.e("newTime", newTimeDrank.toString());
                Log.e("newDate", Long.toString(newDateDrankMilli));
            }
        });

        totalVolume = calculateTotalVolume(newVolumeCount);
        drinkHeaderText.setText("You've drank " + totalVolume.toString() + " oz of fluids so far today!");
    }

    public Integer calculateTotalVolume(Integer newVolumeCount) {

        totalVolume = 0;
        for (int i =0; i < realm.where(Drink.class).findAll().size(); i++) {
            totalVolume = totalVolume + realm.where(Drink.class).findAll().get(i).getVolume();
        }
        totalVolume = totalVolume + newVolumeCount;

        Log.e("drinkHeader", totalVolume.toString());
        return totalVolume;
    }
}
