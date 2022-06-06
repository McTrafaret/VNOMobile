package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is sent to request the location with specific id
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "RAD", numOfArguments = 1)
public class RADCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;
}
