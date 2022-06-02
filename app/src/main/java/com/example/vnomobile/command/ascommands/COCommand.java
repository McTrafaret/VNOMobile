package com.example.vnomobile.command.ascommands;

import com.example.vnomobile.command.BaseCommand;
import com.example.vnomobile.command.Command;
import com.example.vnomobile.command.CommandArgument;
import com.example.vnomobile.command.CommandType;
import com.example.vnomobile.util.BytesToHexStringConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Command(name = "CO", numOfArguments = 2)
public class COCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String username;

    @CommandArgument(index = 1, optional = false)
    String passwordHash;

    public COCommand() {
        super(CommandType.CO.toString());
    }

    public COCommand(String name, String username, String passwordPlain) throws NoSuchAlgorithmException {
        super(name);
        this.username = username;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(passwordPlain.getBytes());
        this.passwordHash = BytesToHexStringConverter.convert(digest).toUpperCase(Locale.ROOT);
    }
}
