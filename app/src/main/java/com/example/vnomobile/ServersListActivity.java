package com.example.vnomobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ServersListActivity extends AppCompatActivity {

    RecyclerView listOfServersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);
        this.listOfServersView = findViewById(R.id.list_of_servers_view);
    }
}