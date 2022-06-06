package com.example.vnolib.command.ascommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by master server after connect
 */
@ToString
@Command(name = "CV")
public class CVCommand extends BaseCommand {

}
