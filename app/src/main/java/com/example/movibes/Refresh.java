package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;

public class Refresh extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView tvRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvRefresh = findViewById(R.id.tvRefresh);

        tvRefresh.setText("Not refreshed");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvRefresh.setText("I am fresh. Refreshed");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}