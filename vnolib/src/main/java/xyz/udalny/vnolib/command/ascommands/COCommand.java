package xyz.udalny.vnolib.command.ascommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;
import xyz.udalny.vnolib.util.BytesToHexStringConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import lombok.ToString;

/**
 * This command is used to authenticate at master server
 */
@ToString
@Command(name = "CO", numOfArguments = 2)
public class COCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String username;

    @CommandArgument(index = 1, optional = false)
    String passwordHash;

    public COCommand(String username, String passwordPlain) throws NoSuchAlgorithmException {
        this.username = username;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(passwordPlain.getBytes());
        this.passwordHash = BytesToHexStringConverter.convert(digest).toUpperCase(Locale.ROOT);
    }
}
