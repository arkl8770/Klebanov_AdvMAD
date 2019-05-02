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
public class ExerciseFragment extends Fragment {

    private Realm realm;
    private ExerciseAdapter exerciseAdapter;

    private Integer totalDuration;

    private TextView exerciseHeaderText;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Food objects
        RealmResults<Exercise> exercises = realm.where(Exercise.class).findAll();
        //exercises = exercises.sort("timeStarted", Sort.DESCENDING);

        //calculates totalDuration upon fragment load
        totalDuration = 0;
        for (int i=0; i < exercises.size(); i++) {
            totalDuration = totalDuration + exercises.get(i).getDuration();
        }

        exerciseAdapter = new ExerciseAdapter(exercises, this);

        View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);

        exerciseHeaderText = rootView.findViewById(R.id.exerciseHeaderText);
        long minutes = totalDuration;
        long hours = totalDuration/60;

        minutes = minutes - 60*hours;
        exerciseHeaderText.setText("You've spent " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes working out so far today!");

        RecyclerView recyclerView = rootView.findViewById(R.id.exerciseRecyclerView);

        //assign the adapter to the recyclerView
        recyclerView.setAdapter(exerciseAdapter);

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
                nameEditText.setHint("Exercise name");
                layout.addView(nameEditText);
                final EditText durationEditText = new EditText(getActivity());
                durationEditText.setHint("Exercise Duration (min)");
                durationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(durationEditText);
                final TimePicker timePicker = new TimePicker(getActivity());
                layout.addView(timePicker);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Add Exercise");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get exercise name entered as String
                        final String newExerciseName = nameEditText.getText().toString();

                        //get duration entered as Integer
                        final Integer newDurationCount;
                        if (durationEditText.getText().toString().isEmpty()) {
                            newDurationCount = 0;
                        } else {
                            newDurationCount = Integer.parseInt(durationEditText.getText().toString());
                        }

                        //get timeStarted as Date
                        Date newTimeStarted = Calendar.getInstance().getTime();;
                        final Integer timeHour = timePicker.getCurrentHour();
                        final Integer timeMinute = timePicker.getCurrentMinute();
                        final String timeString = timeHour + ":" + timeMinute;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        try {
                            newTimeStarted = simpleDateFormat.parse(timeString);
                            Log.e("Time", newTimeStarted.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //get dateStarted in milliseconds
                        Date currentDate = new Date();
                        final long newDateStartedMilli = currentDate.getTime();

                        //addExercise using above variables
                        if (!newExerciseName.isEmpty()) {
                            Log.e("Almost", "Attempt to add exercise entry");
                            addExercise(UUID.randomUUID().toString(), newExerciseName, newDurationCount, newTimeStarted, newDateStartedMilli);
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

    public void deleteExercise(final String exerciseId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Exercise.class).equalTo("id", exerciseId).findFirst().deleteFromRealm();
            }
        });
    }

    public void changeExercise(final String exerciseId, final String exerciseName, final Integer durationCount, final Date timeStarted) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Exercise exercise = realm.where(Exercise.class).equalTo("id", exerciseId).findFirst();
                exercise.setName(exerciseName);
                exercise.setDuration(durationCount);
                exercise.setTimeStarted(timeStarted);
            }
        });

        Exercise exercise = realm.where(Exercise.class).equalTo("id", exerciseId).findFirst();
        Integer durationDiff = durationCount - exercise.getDuration();
        totalDuration = calculateTotalDuration(durationDiff);
        long minutes = totalDuration;
        long hours = totalDuration/60;

        minutes = minutes - 60*hours;
        exerciseHeaderText.setText("You've spent " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes working out so far today!");
    }

    public void editExercise(final String exerciseId) {

        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        Exercise exercise = realm.where(Exercise.class).equalTo("id", exerciseId).findFirst();

        //create edit texts and add to layout
        final EditText nameEditText = new EditText(getActivity());
        nameEditText.setText(exercise.getName());
        nameEditText.setHint("Exercise Name");
        layout.addView(nameEditText);
        final EditText durationEditText = new EditText(getActivity());
        durationEditText.setText(exercise.getDuration().toString());
        durationEditText.setHint("Duration (min)");
        durationEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(durationEditText);
        final TimePicker timePicker = new TimePicker(getActivity());
        timePicker.setCurrentHour(exercise.getTimeStarted().getHours());
        timePicker.setCurrentMinute(exercise.getTimeStarted().getMinutes());
        layout.addView(timePicker);

        //create alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Edit Exercise");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save edited Exercise
                //get updated Exercise and its variables

                //get updatedExerciseName entered as String
                final String updatedExerciseName = nameEditText.getText().toString();

                //get updated duration entered as Integer
                final Integer updatedDurationCount;
                if (durationEditText.getText().toString().isEmpty()) {
                    updatedDurationCount = 0;
                } else {
                    updatedDurationCount = Integer.parseInt(durationEditText.getText().toString());
                }

                //get updatedTimeStarted as Date
                Date updatedTimeStarted = Calendar.getInstance().getTime();;
                final Integer timeHour = timePicker.getCurrentHour();
                final Integer timeMinute = timePicker.getCurrentMinute();
                final String timeString = timeHour + ":" + timeMinute;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                try {
                    updatedTimeStarted = simpleDateFormat.parse(timeString);
                    Log.e("Time", updatedTimeStarted.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //editExercise using above variables
                if (!updatedExerciseName.isEmpty()) {
                    Log.e("Almost", "Attempt to edit exercise entry");
                    changeExercise(exerciseId, updatedExerciseName, updatedDurationCount, updatedTimeStarted);
                }
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete Exercise
                deleteExercise(exerciseId);
                totalDuration = calculateTotalDuration(-1*(realm.where(Exercise.class).equalTo("id", exerciseId).findFirst().getDuration()));
                long minutes = totalDuration;
                long hours = totalDuration/60;

                minutes = minutes - 60*hours;
                exerciseHeaderText.setText("You've spent " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes working out so far today!");
            }
        });
        dialog.show();
    }

    public void addExercise(final String newId, final String newExerciseName, final Integer newDurationCount, final Date newTimeStarted, final long newDateStartedMilli) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //create a new realm Exercise object
                Exercise newExercise = realm.createObject(Exercise.class, newId);
                newExercise.setName(newExerciseName);
                newExercise.setDuration(newDurationCount);
                newExercise.setTimeStarted(newTimeStarted);
                newExercise.setDateStartedMilli(newDateStartedMilli);
                Log.e("newExercise", newExerciseName);
                Log.e("newDuration", newDurationCount.toString());
                Log.e("newTime", newTimeStarted.toString());
                Log.e("newDate", Long.toString(newDateStartedMilli));
            }
        });

        totalDuration = calculateTotalDuration(newDurationCount);
        long minutes = totalDuration;
        long hours = totalDuration/60;

        minutes = minutes - 60*hours;
        exerciseHeaderText.setText("You've spent " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes working out so far today!");
    }

    public Integer calculateTotalDuration(Integer newDurationCount) {

        totalDuration = 0;
        for (int i =0; i < realm.where(Exercise.class).findAll().size(); i++) {
            totalDuration = totalDuration + realm.where(Exercise.class).findAll().get(i).getDuration();
        }
        totalDuration = totalDuration + newDurationCount;

        Log.e("exerciseHeader", totalDuration.toString());
        return totalDuration;
    }
}
