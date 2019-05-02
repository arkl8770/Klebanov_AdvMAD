package com.example.fitnesstrackerlite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ExerciseAdapter extends RealmRecyclerViewAdapter<Exercise, ExerciseAdapter.ViewHolder> {

    private ExerciseFragment exerciseFragment;

    public ExerciseAdapter(RealmResults<Exercise> exerciseData, ExerciseFragment exerciseFragment) {

        super(exerciseData, true);
        this.exerciseFragment = exerciseFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExerciseAdapter.ViewHolder viewHolder, int i) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");

        Exercise exercise = getItem(i);
        viewHolder.field1.setText(exercise.getName());
        viewHolder.field2.setText(exercise.getDuration().toString() + " minutes");
        viewHolder.field3.setText(dateFormat.format(exercise.getTimeStarted()).toString());
        viewHolder.field1.setTag(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) viewHolder.field1.getTag();
                Exercise exercise = getItem(pos);
                exerciseFragment.editExercise(exercise.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView field1;
        TextView field2;
        TextView field3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            field1 = itemView.findViewById(R.id.textView);
            field2 = itemView.findViewById(R.id.textView2);
            field3 = itemView.findViewById(R.id.textView3);
        }
    }
}
