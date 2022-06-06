package com.example.vnolib.command.ascommands;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.ClientState;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "VNAL", numOfArguments = 1)
public class VNALCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String username;

    @Override
    public void handle(Client client) {
        client.setUsername(username);
        client.setState(ClientState.CHOOSING_SERVER);
    }
}
