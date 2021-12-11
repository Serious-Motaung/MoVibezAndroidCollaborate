package com.example.movibes;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class EventEstUserAdapter extends RecyclerView.Adapter<EventEstUserAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private Context context;
    private List<Events> list1;

    public EventEstUserAdapter(Context context, List<Events> list, RecyclerViewClickListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.list1 = list;
    }
    @NonNull
    @Override
    public EventEstUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_est_user,parent,false);
        return new EventEstUserAdapter.ViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventEstUserAdapter.ViewHolder holder, int position) {
        Events event = list1.get(position);

        Glide.with(context).load(event.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivEstUserFlyer);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivEstUserFlyer;

        public ViewHolder(@NonNull View itemView,final RecyclerViewClickListener listener) {
            super(itemView);

            ivEstUserFlyer = itemView.findViewById(R.id.ivEstUserFlyer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onRowClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
