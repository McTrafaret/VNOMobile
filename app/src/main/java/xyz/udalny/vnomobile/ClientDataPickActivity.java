package xyz.udalny.vnomobile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.udalny.vnomobile.adapter.ListOfDataDirectoriesAdapter;
import xyz.udalny.vnomobile.adapter.OnDirectoryListener;
import xyz.udalny.vnomobile.exception.NotDataDirectoryException;
import xyz.udalny.vnomobile.resource.DataDirectoriesRepository;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ResourceHandler;
import xyz.udalny.vnomobile.util.FileUtil;

import xyz.udalny.vnomobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientDataPickActivity extends AppCompatActivity implements OnDirectoryListener {

    private RecyclerView listOfDataDirectoriesView;
    private FloatingActionButton addDataDirectoryButton;

    private DataDirectoriesRepository repository;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onDirectoryClick(DataDirectory directory) {
        ResourceHandler.getInstance().init(directory);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

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
                    String path = getPath(result);
                    try {
                        repository.addDirectory(path);
                        showToast(String.format("Successfully added directory %s", path));
                        listOfDataDirectoriesView.getAdapter().notifyDataSetChanged();
                    } catch (NotDataDirectoryException e) {
                        showToast(e.getMessage());
                    }
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

        this.repository = new DataDirectoriesRepository(this);
        this.repository.loadDataFromCache();

        this.listOfDataDirectoriesView = findViewById(R.id.list_of_data_directories_view);
        this.listOfDataDirectoriesView.setAdapter(new ListOfDataDirectoriesAdapter(repository, this));
        this.listOfDataDirectoriesView.setLayoutManager(new LinearLayoutManager(this));
        this.addDataDirectoryButton = findViewById(R.id.add_data_directory_button);
        addDataDirectoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetDirectory.launch(null);
            }
        });
        verifyStoragePermissions(this);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}