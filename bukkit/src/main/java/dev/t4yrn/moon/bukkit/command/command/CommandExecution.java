package dev.t4yrn.moon.bukkit.command.command;

import dev.t4yrn.moon.bukkit.command.argument.CommandArgs;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.List;

@Getter
public class CommandExecution {

    private final CommandService commandService;
    private final CommandSender sender;
    private final List<String> args;
    private final CommandArgs commandArgs;
    private final cCommand cCommand;
    private boolean canExecute = true;

    public CommandExecution(CommandService commandService, CommandSender sender, List<String> args, CommandArgs commandArgs, cCommand cCommand) {
        this.commandService = commandService;
        this.sender = sender;
        this.args = args;
        this.commandArgs = commandArgs;
        this.cCommand = cCommand;
    }

    public void preventExecution() {
        canExecute = false;
    }

}
