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
public class FoodFragment extends Fragment {

    private Realm realm;
    private FoodAdapter foodAdapter;

    private Integer totalCalories;

    private TextView foodHeaderText;

    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Food objects
        RealmResults<Food> foods = realm.where(Food.class).findAll();
        //foods = foods.sort("timeEaten", Sort.DESCENDING);

        //calculates totalCalories upon fragment load
        totalCalories = 0;
        for (int i=0; i < foods.size(); i++) {
            totalCalories = totalCalories + foods.get(i).getCalories();
        }

        foodAdapter = new FoodAdapter(foods, this);

        View rootView = inflater.inflate(R.layout.fragment_food, container, false);

        foodHeaderText = rootView.findViewById(R.id.foodHeaderText);
        foodHeaderText.setText("You've consumed " + totalCalories.toString() + " calories so far today!");

        RecyclerView recyclerView = rootView.findViewById(R.id.foodRecyclerView);

        //assign the adapter to the recyclerView
        recyclerView.setAdapter(foodAdapter);

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
                nameEditText.setHint("Food name");
                layout.addView(nameEditText);
                final EditText caloriesEditText = new EditText(getActivity());
                caloriesEditText.setHint("Calories");
                caloriesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(caloriesEditText);
                final TimePicker timePicker = new TimePicker(getActivity());
                layout.addView(timePicker);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Add Food");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get food name entered as String
                        final String newFoodName = nameEditText.getText().toString();

                        //get number of calories entered as Integer
                        final Integer newCalorieCount;
                        if (caloriesEditText.getText().toString().isEmpty()) {
                            newCalorieCount = 0;
                        } else {
                            newCalorieCount = Integer.parseInt(caloriesEditText.getText().toString());
                        }

                        //get timeEaten as Date
                        Date newTimeEaten = Calendar.getInstance().getTime();;
                        final Integer timeHour = timePicker.getCurrentHour();
                        final Integer timeMinute = timePicker.getCurrentMinute();
                        final String timeString = timeHour + ":" + timeMinute;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        try {
                            newTimeEaten = simpleDateFormat.parse(timeString);
                            Log.e("Time", newTimeEaten.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //get dateEaten in milliseconds
                        Date currentDate = new Date();
                        final long newDateEatenMilli = currentDate.getTime();

                        //addFood using above variables
                        if (!newFoodName.isEmpty()) {
                            Log.e("Almost", "Attempt to add food entry");
                            addFood(UUID.randomUUID().toString(), newFoodName, newCalorieCount, newTimeEaten, newDateEatenMilli);
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

    public void deleteFood(final String foodId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Food.class).equalTo("id", foodId).findFirst().deleteFromRealm();
            }
        });
    }

    public void changeFood(final String foodId, final String foodName, final Integer calorieCount, final Date timeEaten) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Food food = realm.where(Food.class).equalTo("id", foodId).findFirst();
                food.setName(foodName);
                food.setCalories(calorieCount);
                food.setTimeEaten(timeEaten);
            }
        });

        Food food = realm.where(Food.class).equalTo("id", foodId).findFirst();
        Integer calorieDiff = calorieCount - food.getCalories();
        totalCalories = calculateTotalCalories(calorieDiff);
        foodHeaderText.setText("You've consumed " + totalCalories.toString() + " calories so far today!");
    }

    public void editFood(final String foodId) {

        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final Food food = realm.where(Food.class).equalTo("id", foodId).findFirst();

        //create edit texts and add to layout
        final EditText nameEditText = new EditText(getActivity());
        nameEditText.setText(food.getName());
        nameEditText.setHint("Food name");
        layout.addView(nameEditText);
        final EditText caloriesEditText = new EditText(getActivity());
        caloriesEditText.setText(food.getCalories().toString());
        caloriesEditText.setHint("Calories");
        caloriesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(caloriesEditText);
        final TimePicker timePicker = new TimePicker(getActivity());
        timePicker.setCurrentHour(food.getTimeEaten().getHours());
        timePicker.setCurrentMinute(food.getTimeEaten().getMinutes());
        layout.addView(timePicker);

        //create alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Edit Food");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save edited Food
                //get updated Food and its variables

                //get updatedFoodName entered as String
                final String updatedFoodName = nameEditText.getText().toString();

                //get updated number of calories entered as Integer
                final Integer updatedCalorieCount;
                if (caloriesEditText.getText().toString().isEmpty()) {
                    updatedCalorieCount = 0;
                } else {
                    updatedCalorieCount = Integer.parseInt(caloriesEditText.getText().toString());
                }

                //get updatedTimeEaten as Date
                Date updatedTimeEaten = Calendar.getInstance().getTime();;
                final Integer timeHour = timePicker.getCurrentHour();
                final Integer timeMinute = timePicker.getCurrentMinute();
                final String timeString = timeHour + ":" + timeMinute;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    updatedTimeEaten = simpleDateFormat.parse(timeString);
                    Log.e("Time", updatedTimeEaten.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //editFood using above variables
                if (!updatedFoodName.isEmpty()) {
                    Log.e("Almost", "Attempt to edit food entry");
                    changeFood(foodId, updatedFoodName, updatedCalorieCount, updatedTimeEaten);
                }
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete Food
                deleteFood(foodId);
                totalCalories = calculateTotalCalories(-1*(realm.where(Food.class).equalTo("id", foodId).findFirst().getCalories()));
                foodHeaderText.setText("You've consumed " + totalCalories.toString() + " calories so far today!");

            }
        });
        dialog.show();
    }

    public void addFood(final String newId, final String newFoodName, final Integer newCalorieCount, final Date newTimeEaten, final long newDateEatenMilli) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //create a new realm Food object
                Food newFood = realm.createObject(Food.class, newId);
                newFood.setName(newFoodName);
                newFood.setCalories(newCalorieCount);
                newFood.setTimeEaten(newTimeEaten);
                newFood.setDateEatenMilli(newDateEatenMilli);
                Log.e("newFood", newFoodName);
                Log.e("newCalories", newCalorieCount.toString());
                Log.e("newTime", newTimeEaten.toString());
                Log.e("newDate", Long.toString(newDateEatenMilli));
            }
        });

        totalCalories = calculateTotalCalories(newCalorieCount);
        foodHeaderText.setText("You've consumed " + totalCalories.toString() + " calories so far today!");
    }

    public Integer calculateTotalCalories(Integer newCalorieCount) {

        totalCalories = 0;
        for (int i =0; i < realm.where(Food.class).findAll().size(); i++) {
            totalCalories = totalCalories + realm.where(Food.class).findAll().get(i).getCalories();
        }
        totalCalories = totalCalories + newCalorieCount;

        Log.e("foodHeader", totalCalories.toString());
        return totalCalories;
    }
}
