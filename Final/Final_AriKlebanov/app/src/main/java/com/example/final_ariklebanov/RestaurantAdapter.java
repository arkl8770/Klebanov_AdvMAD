package com.example.final_ariklebanov;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class RestaurantAdapter extends RealmRecyclerViewAdapter<Restaurant, RestaurantAdapter.ViewHolder> {

    private MainActivity activity;

    public RestaurantAdapter(RealmResults<Restaurant> data, MainActivity activity) {
        super(data, true);
        this.activity = activity;
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
    public void onBindViewHolder(@NonNull final RestaurantAdapter.ViewHolder viewHolder, int i) {
        Restaurant restaurant = getItem(i);
        viewHolder.textView.setText(restaurant.getName());
        viewHolder.textView.setTag(i);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) viewHolder.textView.getTag();
                Restaurant restaurant = getItem(pos);
                //activity.editRestaurant(restaurant.getId());
                activity.onListItemClick(restaurant.getId());
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = (Integer) viewHolder.textView.getTag();
                Restaurant restaurant = getItem(pos);
                activity.editRestaurant(restaurant.getId());
                return true;
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}