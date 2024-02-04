package dev.t4yrn.moon.bukkit.command.command;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

@Getter
@Setter
public class Authorizer {

    private String noPermissionMessage = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.";

    public boolean isAuthorized(@Nonnull CommandSender sender, @Nonnull cCommand cCommand) {
        if (cCommand.getPermission() != null && cCommand.getPermission().length() > 0) {
            if (!sender.hasPermission(cCommand.getPermission())) {
                sender.sendMessage(noPermissionMessage);
                return false;
            }
        }
        return true;
    }

}
