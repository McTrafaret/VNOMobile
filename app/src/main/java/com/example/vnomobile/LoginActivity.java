package com.example.vnomobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vnolib.client.OnCommand;
import com.example.vnolib.command.ascommands.NoCommand;
import com.example.vnolib.command.ascommands.VNALCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginActivity extends AppCompatActivity {

    private EditText loginInput;
    private EditText passwordInput;
    private Button identifyButton;
    private Button createAccountButton;
    private CheckBox rememberMeCheckbox;

    private static final String REMEMBER_ME_FILE = "remember_me.txt";

    private void saveCredentials() {
        try(FileWriter writer = new FileWriter(new File(getCacheDir(), REMEMBER_ME_FILE))) {
            Boolean remember = rememberMeCheckbox != null && rememberMeCheckbox.isChecked();
            writer.append(String.format("%s%n", remember));
            writer.append(String.format("%s%n", loginInput.getText().toString()));
            writer.append(String.format("%s%n", passwordInput.getText().toString()));
        } catch (IOException e) {
            log.error("Error while writing credentials to {}:", REMEMBER_ME_FILE, e);
        }
    }

    private void loadCredentials() {
        try(FileReader fileReader = new FileReader(new File(getCacheDir(), REMEMBER_ME_FILE))) {
            BufferedReader reader = new BufferedReader(fileReader);
            boolean remember = Boolean.parseBoolean(reader.readLine());
            if(!remember) {
                return;
            }
            String login = reader.readLine();
            String password = reader.readLine();

            loginInput.setText(login);
            passwordInput.setText(password);
            rememberMeCheckbox.setChecked(true);
        } catch (FileNotFoundException e) {
            log.warn("{} not found", REMEMBER_ME_FILE);
        } catch (IOException e) {
            log.error("Error while reading credentials from {}:", REMEMBER_ME_FILE, e);
        }
    }

    @OnCommand(VNALCommand.class)
    public void authenticated(VNALCommand command) {
        log.info("Got VNAL");
        saveCredentials();
        ClientHandler.getClient().unsubscribeFromCommand(VNALCommand.class, this);
        ClientHandler.getClient().unsubscribeFromCommand(NoCommand.class, this);
        Intent intent = new Intent(this, ServersListActivity.class);
        startActivity(intent);
    }

    @OnCommand(NoCommand.class)
    public void wrongCredentials(NoCommand command) {
        log.info("Got No");
        Toast.makeText(this, R.string.wrong_credentials, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        try {
            ClientHandler.getClient().connectToMaster();
        } catch (Exception ex) {
            log.error("LoginActivity: ", ex);
        }

        ClientHandler.getClient().subscribeToCommand(VNALCommand.class, this);
        ClientHandler.getClient().subscribeToCommand(NoCommand.class, this);



        loginInput = findViewById(R.id.login_input);
        passwordInput = findViewById(R.id.password_input);
        identifyButton = findViewById(R.id.identify_button);
        createAccountButton = findViewById(R.id.create_account_button);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);

        loadCredentials();

        identifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginInput.getText().toString();
                String password = passwordInput.getText().toString();
                try {
                    ClientHandler.getClient().authenticate(login, password);
                } catch (Exception ex) {
                    log.error("While authenticating on master: ", ex);
                }
            }
        });
    }
}