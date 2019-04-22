package com.example.albumlist_realm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class AlbumAdapter extends RealmRecyclerViewAdapter<Album, AlbumAdapter.ViewHolder> {

    private MainActivity activity;

    public AlbumAdapter(RealmResults<Album> data, MainActivity activity) {
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
    public void onBindViewHolder(@NonNull final AlbumAdapter.ViewHolder viewHolder, int i) {
        Album album = getItem(i);
        viewHolder.albumName.setText(album.getAlbumName());
        viewHolder.artistName.setText(album.getArtistName());
        viewHolder.listened.setChecked(album.getListened());
        viewHolder.listened.setTag(i);

        viewHolder.listened.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //makes sure this is only called for the current checkbox
                //solved the problem of this being called too many times
                if(buttonView.isShown()) {
                    int pos = (Integer) viewHolder.listened.getTag();
                    Album album = getItem(pos);
                    activity.changeAlbumListened(album.getId());
                }

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer) viewHolder.listened.getTag();
                Album album = getItem(pos);
                activity.editAlbum(album.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        TextView artistName;
        CheckBox listened;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.albumTextView);
            artistName = itemView.findViewById(R.id.artistTextView);
            listened = itemView.findViewById(R.id.checkBox);
        }
    }
}
