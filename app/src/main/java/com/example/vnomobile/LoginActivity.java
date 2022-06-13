package com.example.vnomobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vnolib.client.OnCommand;
import com.example.vnolib.command.ascommands.NoCommand;
import com.example.vnolib.command.ascommands.VNALCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginActivity extends AppCompatActivity {

    private EditText loginInput;
    private EditText passwordInput;
    private Button identifyButton;
    private Button createAccountButton;

    @OnCommand(VNALCommand.class)
    public void authenticated(VNALCommand command) {
        log.info("Got VNAL");
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