package com.example.fitnesstrackerlite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class SleepAdapter extends RealmRecyclerViewAdapter<Sleep, SleepAdapter.ViewHolder> {

    private SleepFragment sleepFragment;

    public SleepAdapter(RealmResults<Sleep> sleepData, SleepFragment sleepFragment) {

        super(sleepData, true);
        this.sleepFragment = sleepFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item_sleep, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SleepAdapter.ViewHolder viewHolder, int i) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

        Sleep sleep = getItem(i);
        viewHolder.field1.setText("Start time: " + dateFormat.format(sleep.getTimeStarted()));
        viewHolder.field2.setText("End time: " + dateFormat.format(sleep.getTimeEnded()));
        viewHolder.ratingBar.setRating(sleep.getRating());
        viewHolder.field1.setTag(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) viewHolder.field1.getTag();
                Sleep sleep = getItem(pos);
                sleepFragment.editSleep(sleep.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView field1;
        TextView field2;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            field1 = itemView.findViewById(R.id.textView);
            field2 = itemView.findViewById(R.id.textView2);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
