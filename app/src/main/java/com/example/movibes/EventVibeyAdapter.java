package com.example.movibes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class EventVibeyAdapter extends RecyclerView.Adapter<EventVibeyAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private Context context;
    private List<Events> list1;

    public EventVibeyAdapter(Context context, List<Events> list, RecyclerViewClickListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.list1 = list;
    }
    @NonNull
    @Override
    public EventVibeyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_vibey,parent,false);
        return new EventVibeyAdapter.ViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventVibeyAdapter.ViewHolder holder, int position) {
        Events event = list1.get(position);

        Glide.with(context).load(event.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivVibeyUserFlyer);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivVibeyUserFlyer;

        public ViewHolder(@NonNull View itemView,final RecyclerViewClickListener listener) {
            super(itemView);

            ivVibeyUserFlyer = itemView.findViewById(R.id.ivVibeyUserFlyer);

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
