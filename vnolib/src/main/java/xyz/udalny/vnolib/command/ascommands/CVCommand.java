package xyz.udalny.vnolib.command.ascommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by master server after connect
 */
@ToString
@Command(name = "CV")
public class CVCommand extends BaseCommand {

}
