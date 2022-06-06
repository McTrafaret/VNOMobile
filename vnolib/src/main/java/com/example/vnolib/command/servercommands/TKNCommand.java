package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by server in response to {@link ReqCommand} if character pick is not allowed.
 */
@ToString
@Command(name = "TKN")
public class TKNCommand extends BaseCommand {
}
