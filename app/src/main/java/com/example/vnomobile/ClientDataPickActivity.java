package com.example.vnomobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClientDataPickActivity extends AppCompatActivity {

    private RecyclerView listOfDataDirectoriesView;
    private FloatingActionButton addDataDirectoryButton;

    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_data_pick);
        this.listOfDataDirectoriesView = findViewById(R.id.list_of_data_directories_view);
        this.addDataDirectoryButton = findViewById(R.id.add_data_directory_button);
        addDataDirectoryButton.setOnClickListener();
    }


}