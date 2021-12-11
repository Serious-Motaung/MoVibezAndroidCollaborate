package com.example.movibes;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private Context context;
    private List<Events> list;

    public EventAdapter(Context context,List<Events> list,RecyclerViewClickListener listener)
    {
        this.listener = listener;
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
        return new EventAdapter.ViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Events event = list.get(position);

        holder.tvDescription.setText(event.getDescription());
        holder.tvDatePosted.setText(event.getDatePosted());
        holder.tvVenue.setText(event.getVenue());
        holder.tvVibes.setText(event.getEventVibes());
        holder.tvReviews.setText(event.getEventReviews());

        Glide.with(context).load(event.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivFlyer);
        Glide.with(context).load(event.getProfilePic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivProfilePic);
        holder.ibVibes.setImageResource(R.drawable.ic_vibe);
        holder.ibVibes.setColorFilter(holder.ibVibes.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibReviews.setImageResource(R.drawable.ic_review);
        holder.ibReviews.setColorFilter(holder.ibReviews.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        holder.ibShare.setImageResource(R.drawable.ic_share);
        holder.ibShare.setColorFilter(holder.ibShare.getContext().getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvVenue,tvDatePosted,tvDescription,tvVibes,tvReviews;
        public ImageView ivFlyer,ivProfilePic;
        public ImageButton ibVibes,ibReviews,ibShare;

        public ViewHolder(@NonNull View itemView,final RecyclerViewClickListener listener) {
            super(itemView);

            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDatePosted = itemView.findViewById(R.id.tvPostedDate);
            tvVenue = itemView.findViewById(R.id.tvEstablishment);
            ivFlyer = itemView.findViewById(R.id.ivFlyer);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvVibes = itemView.findViewById(R.id.tvVibes);
            tvReviews = itemView.findViewById(R.id.tvReviews);
            ibVibes = itemView.findViewById(R.id.ibVibes);
            ibReviews = itemView.findViewById(R.id.ibReviews);
            ibShare = itemView.findViewById(R.id.ibShare);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onRowClicked(getAdapterPosition());
                    }
                }
            });
            ibVibes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                            listener.onVibeClicked(v, getAdapterPosition());
                            ibVibes.setColorFilter(ibVibes.getContext().getResources().getColor(R.color.teal_200), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            });
            ibReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onReviewClicked(v, getAdapterPosition());
                    }
                }
            });
            ibShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onShareClicked(v, getAdapterPosition());
                    }
                }
            });
            tvVenue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        listener.onVenueClicked(getAdapterPosition());
                    }
                }
            });
        }
    }
}
