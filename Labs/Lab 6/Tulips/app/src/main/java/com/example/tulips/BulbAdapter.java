package com.example.tulips;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class BulbAdapter extends RecyclerView.Adapter<BulbAdapter.ViewHolder> {

    private List<Bulb> mBulbs;

    public BulbAdapter(List<Bulb> bulbs, listItemClickListener bulbClickListener) {
        mBulbs = bulbs;
        itemClickListener = bulbClickListener;
    }

    @NonNull
    @Override
    public BulbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View bulbView = inflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(bulbView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BulbAdapter.ViewHolder viewHolder, int i) {
        Bulb bulb = mBulbs.get(i);
        viewHolder.bulbTextView.setText(bulb.getName());
    }

    @Override
    public int getItemCount() {
        return mBulbs.size();
    }

    public interface listItemClickListener {
        void onListItem(int position);
    }

    listItemClickListener itemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bulbTextView;

        //constructor method
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bulbTextView = itemView.findViewById(R.id.textView);
            bulbTextView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onListItem(getAdapterPosition());

        }
    }
}
