package com.example.movibes;

import android.view.View;

public interface RecyclerViewClickListener {
    void onSuccess(String result);
    void onRowClicked(int position);
    void onVibeClicked(View v, int position);
    void onReviewClicked(View review,int position);
    void onShareClicked(View review,int position);
    void onDeleteClicked(View review,int position);
    void onEditClicked(View review,int position);
    void onVenueClicked(int position);
}
