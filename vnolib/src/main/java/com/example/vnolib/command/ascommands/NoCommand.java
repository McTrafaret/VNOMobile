package com.example.vnolib.command.ascommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by master if authentication failed
 */
@ToString
@Command(name = "No")
public class NoCommand extends BaseCommand {
}
