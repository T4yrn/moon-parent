package dev.t4yrn.moon.bukkit.command.command;

import org.bukkit.command.CommandSender;

public interface HelpFormatter {

    void sendHelpFor(CommandSender sender, CommandContainer container);
}
