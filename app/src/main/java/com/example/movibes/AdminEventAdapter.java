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

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private Context context;
    private List<Events> list;

    public AdminEventAdapter(Context context, List<Events> list, RecyclerViewClickListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public AdminEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list_admin,parent,false);
        return new AdminEventAdapter.ViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEventAdapter.ViewHolder holder, int position) {
        Events event = list.get(position);

        holder.tvAdminDescription.setText(event.getDescription());
        holder.tvAdminVibes.setText(event.getEventVibes());
        holder.tvAdminReviews.setText(event.getEventReviews());

        Glide.with(context).load(event.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivAdminFlyer);
        holder.ibAdminVibes.setImageResource(R.drawable.ic_vibe);
        holder.ibAdminVibes.setColorFilter(holder.ibAdminVibes.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibAdminReviews.setImageResource(R.drawable.ic_review);
        holder.ibAdminReviews.setColorFilter(holder.ibAdminReviews.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibAdminShare.setImageResource(R.drawable.ic_share);
        holder.ibAdminShare.setColorFilter(holder.ibAdminShare.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibAdminDelete.setImageResource(R.drawable.ic_delete);
        holder.ibAdminDelete.setColorFilter(holder.ibAdminDelete.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibAdminEdit.setImageResource(R.drawable.ic_edit);
        holder.ibAdminEdit.setColorFilter(holder.ibAdminEdit.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAdminDescription,tvAdminVibes,tvAdminReviews;
        public ImageView ivAdminFlyer;
        public ImageButton ibAdminVibes,ibAdminReviews,ibAdminShare,ibAdminDelete,ibAdminEdit;

        public ViewHolder(@NonNull View itemView,final RecyclerViewClickListener listener) {
            super(itemView);

            tvAdminDescription = itemView.findViewById(R.id.tvAdminDescription);
            ivAdminFlyer = itemView.findViewById(R.id.ivAdminFlyer);
            tvAdminVibes = itemView.findViewById(R.id.tvAdminVibes);
            ibAdminVibes = itemView.findViewById(R.id.ibAdminVibes);
            tvAdminReviews = itemView.findViewById(R.id.tvAdminReviews);
            ibAdminReviews = itemView.findViewById(R.id.ibAdminReviews);
            ibAdminShare = itemView.findViewById(R.id.ibAdminShare);
            ibAdminDelete = itemView.findViewById(R.id.ibAdminDelete);
            ibAdminEdit = itemView.findViewById(R.id.ibAdminEdit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onRowClicked(getAdapterPosition());
                    }
                }
            });
            ibAdminVibes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                            listener.onVibeClicked(v, getAdapterPosition());
                            ibAdminVibes.setColorFilter(ibAdminVibes.getContext().getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            });
            ibAdminReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onReviewClicked(v, getAdapterPosition());
                    }
                }
            });
            ibAdminShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onShareClicked(v, getAdapterPosition());
                    }
                }
            });
            ibAdminDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onDeleteClicked(v, getAdapterPosition());
                    }
                }
            });
            ibAdminEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onEditClicked(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
