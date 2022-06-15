package com.example.vnomobile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnomobile.adapter.ListOfDataDirectoriesAdapter;
import com.example.vnomobile.resource.DataDirectoriesRepository;
import com.example.vnomobile.util.FileUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;

public class ClientDataPickActivity extends AppCompatActivity {

    private RecyclerView listOfDataDirectoriesView;
    private FloatingActionButton addDataDirectoryButton;

    private HashSet<String> dataPaths;

    private class MyOpenDocumentTree extends ActivityResultContracts.OpenDocumentTree {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @Nullable Uri input) {
            Intent intent = super.createIntent(context, input);
            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                            | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
            return intent;
        }
    }


    private ActivityResultLauncher<Uri> mGetDirectory = registerForActivityResult(new MyOpenDocumentTree(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
//                    Uri uriTree = DocumentsContract.buildDocumentUriUsingTree(result, DocumentsContract.getTreeDocumentId(result));
//                    Cursor cursor = getContentResolver().query(uriTree, null, null, null, null);
//                    cursor.moveToFirst();
//                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    String path = cursor.getString(nameIndex);
                    String path = getPath(result);
                    DataDirectoriesRepository.getInstance().add(path);
                    listOfDataDirectoriesView.getAdapter().notifyDataSetChanged();
                }
            });

    private static final int REQUEST_CODE = 0;

    private String getPath(Uri uri) {
        return FileUtil.getFullPathFromTreeUri(uri, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_data_pick);
        this.listOfDataDirectoriesView = findViewById(R.id.list_of_data_directories_view);
        this.listOfDataDirectoriesView.setAdapter(new ListOfDataDirectoriesAdapter());
        this.listOfDataDirectoriesView.setLayoutManager(new LinearLayoutManager(this));
        this.addDataDirectoryButton = findViewById(R.id.add_data_directory_button);
        dataPaths = new HashSet<>();
        addDataDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetDirectory.launch(null);
            }
        });
    }

    private void directoryAdded(String path) {
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
    }

}