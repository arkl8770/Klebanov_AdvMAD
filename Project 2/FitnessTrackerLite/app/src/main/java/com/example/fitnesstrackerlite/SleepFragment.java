package com.example.fitnesstrackerlite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.resources.TextAppearance;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.zip.Inflater;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class SleepFragment extends Fragment {

    private Realm realm;
    private SleepAdapter sleepAdapter;

    private long totalSleep;

    private TextView sleepHeaderText;

    public SleepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get realm instance
        realm = Realm.getDefaultInstance();

        //get all saved Sleep objects
        RealmResults<Sleep> sleeps = realm.where(Sleep.class).findAll();
        //sleeps = sleeps.sort("dateSleptMilli", Sort.DESCENDING);

        //calculates totalSleep upon fragment load
        totalSleep = 0;
        for (int i =0; i < realm.where(Sleep.class).findAll().size(); i++) {
            totalSleep = totalSleep + getSleepTime(realm.where(Sleep.class).findAll().get(i).getTimeStarted(), realm.where(Sleep.class).findAll().get(i).getTimeEnded());
        }
        long seconds = totalSleep/1000;
        long minutes = seconds/60;
        long hours = minutes/60;

        minutes = minutes - 60*hours;

        sleepAdapter = new SleepAdapter(sleeps, this);

        View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        sleepHeaderText = rootView.findViewById(R.id.sleepHeaderText);

        sleepHeaderText.setText("You've slept " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes so far this week!");


        RecyclerView recyclerView = rootView.findViewById(R.id.sleepRecyclerView);

        //assign the adapter to the recyclerView
        recyclerView.setAdapter(sleepAdapter);

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

                //set Layout parameters for textViews above timePickers
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textParams.setMargins(80, 50, 0, 0);

                //set Layout parameters for ratingBar
                LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ratingParams.gravity = Gravity.CENTER;

                //create timePickers, textViews, and ratingBar and add to layout
                final TextView sleepStartText = new TextView(getActivity());
                sleepStartText.setText("Start time:");
                sleepStartText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
                layout.addView(sleepStartText, textParams);
                final TimePicker sleepStartPicker = new TimePicker(getActivity(), null, R.layout.time_picker);
                layout.addView(sleepStartPicker);
                final TextView sleepEndText = new TextView(getActivity());
                sleepEndText.setText("End time:");
                sleepEndText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
                layout.addView(sleepEndText, textParams);
                final TimePicker sleepEndPicker = new TimePicker(getActivity(), null, R.layout.time_picker);
                layout.addView(sleepEndPicker);
                final TextView ratingBarText = new TextView(getActivity());
                ratingBarText.setText("Rating (0-5):");
                ratingBarText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
                layout.addView(ratingBarText, textParams);
                final RatingBar ratingBar = new RatingBar(getActivity());
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(1);
                layout.addView(ratingBar, ratingParams);

                //create alert dialog
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Add Sleep");
                dialog.setView(layout);
                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                        //get timeStarted as Date
                        Date newTimeStarted = Calendar.getInstance().getTime();

                        final Integer timeStartHour = sleepStartPicker.getCurrentHour();
                        final Integer timeStartMinute = sleepStartPicker.getCurrentMinute();
                        final String timeStartString = timeStartHour + ":" + timeStartMinute;

                        try {
                            newTimeStarted = simpleDateFormat.parse(timeStartString);
                            Log.e("TimeStarted", newTimeStarted.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //get timeEnded as Date
                        Date newTimeEnded = Calendar.getInstance().getTime();
                        ;
                        final Integer timeEndHour = sleepEndPicker.getCurrentHour();
                        final Integer timeEndMinute = sleepEndPicker.getCurrentMinute();
                        final String timeEndString = timeEndHour + ":" + timeEndMinute;

                        try {
                            newTimeEnded = simpleDateFormat.parse(timeEndString);
                            Log.e("TimeEnded", newTimeEnded.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //get rating as Integer
                        Integer newRating = Math.round(ratingBar.getRating());

                        //get dateEaten in milliseconds
                        Date currentDate = new Date();
                        final long newDateSleptMilli = currentDate.getTime();

                        //addSleep using above variables
                        addSleep(UUID.randomUUID().toString(), newTimeStarted, newTimeEnded, newRating, newDateSleptMilli);

                        getSleepTime(newTimeStarted, newTimeEnded);
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

    public void deleteSleep(final String sleepId) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Sleep.class).equalTo("id", sleepId).findFirst().deleteFromRealm();
            }
        });
    }

    public void changeSleep(final String sleepId, final Date timeStarted, final Date timeEnded, final Integer rating) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Sleep sleep = realm.where(Sleep.class).equalTo("id", sleepId).findFirst();
                sleep.setTimeStarted(timeStarted);
                sleep.setTimeEnded(timeEnded);
                sleep.setRating(rating);
            }
        });

        Sleep sleep = realm.where(Sleep.class).equalTo("id", sleepId).findFirst();
        long sleepDiff = getSleepTime(timeStarted, timeEnded) - getSleepTime(sleep.getTimeStarted(), sleep.getTimeEnded());
        totalSleep = calculateTotalSleep(sleepDiff);
        long seconds = totalSleep/1000;
        long minutes = seconds/60;
        long hours = minutes/60;

        minutes = minutes - 60*hours;
        sleepHeaderText.setText("You've slept " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes so far this week!");
    }

    public void editSleep(final String sleepId) {

        //create a vertical linear layout to hold edit texts
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        //set Layout parameters for textViews above timePickers
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(80, 50, 0, 0);

        //set Layout parameters for ratingBar
        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingParams.gravity = Gravity.CENTER;

        Sleep sleep = realm.where(Sleep.class).equalTo("id", sleepId).findFirst();

        //create timePickers, textViews, and ratingBar and add to layout
        final TextView sleepStartText = new TextView(getActivity());
        sleepStartText.setText("Start time:");
        sleepStartText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        layout.addView(sleepStartText, textParams);
        final TimePicker sleepStartPicker = new TimePicker(getActivity(), null, R.layout.time_picker);
        sleepStartPicker.setCurrentHour(sleep.getTimeStarted().getHours());
        sleepStartPicker.setCurrentMinute(sleep.getTimeStarted().getMinutes());
        layout.addView(sleepStartPicker);
        final TextView sleepEndText = new TextView(getActivity());
        sleepEndText.setText("End time:");
        sleepEndText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        layout.addView(sleepEndText, textParams);
        final TimePicker sleepEndPicker = new TimePicker(getActivity(), null, R.layout.time_picker);
        sleepEndPicker.setCurrentHour(sleep.getTimeEnded().getHours());
        sleepEndPicker.setCurrentMinute(sleep.getTimeEnded().getMinutes());
        layout.addView(sleepEndPicker);
        final TextView ratingBarText = new TextView(getActivity());
        ratingBarText.setText("Rating (0-5):");
        ratingBarText.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        layout.addView(ratingBarText, textParams);
        final RatingBar ratingBar = new RatingBar(getActivity());
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        ratingBar.setRating(sleep.getRating());
        layout.addView(ratingBar, ratingParams);

        //create alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Edit Sleep");
        dialog.setView(layout);
        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //save edited Sleep
                //get updated Sleep and its variables

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                //get timeStarted as Date
                Date updatedTimeStarted = Calendar.getInstance().getTime();

                final Integer timeStartHour = sleepStartPicker.getCurrentHour();
                final Integer timeStartMinute = sleepStartPicker.getCurrentMinute();
                final String timeStartString = timeStartHour + ":" + timeStartMinute;

                try {
                    updatedTimeStarted = simpleDateFormat.parse(timeStartString);
                    Log.e("TimeStarted", updatedTimeStarted.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //get timeEnded as Date
                Date updatedTimeEnded = Calendar.getInstance().getTime();
                ;
                final Integer timeEndHour = sleepEndPicker.getCurrentHour();
                final Integer timeEndMinute = sleepEndPicker.getCurrentMinute();
                final String timeEndString = timeEndHour + ":" + timeEndMinute;

                try {
                    updatedTimeEnded = simpleDateFormat.parse(timeEndString);
                    Log.e("TimeEnded", updatedTimeEnded.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //get rating as Integer
                Integer updatedRating = Math.round(ratingBar.getRating());

                //editSleep using above variables
                changeSleep(sleepId, updatedTimeStarted, updatedTimeEnded, updatedRating);
            }
        });
        dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete Sleep
                deleteSleep(sleepId);
                long sleepDiff = getSleepTime(realm.where(Sleep.class).equalTo("id", sleepId).findFirst().getTimeStarted(), realm.where(Sleep.class).equalTo("id", sleepId).findFirst().getTimeEnded());
                totalSleep = calculateTotalSleep(-1*(sleepDiff));
                long seconds = totalSleep/1000;
                long minutes = seconds/60;
                long hours = minutes/60;

                minutes = minutes - 60*hours;
                sleepHeaderText.setText("You've slept " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes so far this week!");
            }
        });
        dialog.show();
    }

    public void addSleep(final String newId, final Date newTimeStarted, final Date newTimeEnded, final Integer newRating, final long newDateSleptMilli) {

        //start realm write transaction
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                //create a new realm Food object
                Sleep newSleep = realm.createObject(Sleep.class, newId);
                newSleep.setTimeStarted(newTimeStarted);
                newSleep.setTimeEnded(newTimeEnded);
                newSleep.setRating(newRating);
                newSleep.setDateSleptMilli(newDateSleptMilli);
                Log.e("newTimeStarted", newTimeStarted.toString());
                Log.e("newTimeEnded", newTimeEnded.toString());
                Log.e("newRating", newRating.toString());
                Log.e("newDate", Long.toString(newDateSleptMilli));
            }
        });

        totalSleep = calculateTotalSleep(newTimeStarted, newTimeEnded);
        long seconds = totalSleep/1000;
        long minutes = seconds/60;
        long hours = minutes/60;

        minutes = minutes - 60*hours;
        sleepHeaderText.setText("You've slept " + Long.toString(hours) + " hours and " + Long.toString(minutes) + " minutes so far this week!");
    }

    public long calculateTotalSleep(Date newSleepStartTime, Date newSleepEndTime) {

        totalSleep = 0;
        for (int i = 0; i < realm.where(Sleep.class).findAll().size(); i++) {
            totalSleep = totalSleep + getSleepTime(realm.where(Sleep.class).findAll().get(i).getTimeStarted(), realm.where(Sleep.class).findAll().get(i).getTimeEnded());
        }
        totalSleep = totalSleep + getSleepTime(newSleepStartTime, newSleepEndTime);

        Log.e("sleepHeader", Long.toString(totalSleep));
        return totalSleep;
    }

    public long calculateTotalSleep(long sleepDiff) {
        totalSleep = 0;
        for (int i = 0; i < realm.where(Sleep.class).findAll().size(); i++) {
            totalSleep = totalSleep +  getSleepTime(realm.where(Sleep.class).findAll().get(i).getTimeStarted(), realm.where(Sleep.class).findAll().get(i).getTimeEnded());
        }
        totalSleep = totalSleep + sleepDiff;

        Log.e("sleepHeader", Long.toString(totalSleep));
        return totalSleep;
    }

    public long getSleepTime(Date sleepStart, Date sleepEnd) {

        long diffMilli = 0;
        if (sleepEnd.getTime() > sleepStart.getTime()) {
            diffMilli = sleepEnd.getTime() - sleepStart.getTime();
        } else {
            Date midnightDate = new Date();
            midnightDate.setTime(111599000);

            diffMilli = (midnightDate.getTime() - sleepStart.getTime());
            diffMilli = diffMilli + sleepEnd.getTime() - 25140000;
        }
        return diffMilli;
    }
}
