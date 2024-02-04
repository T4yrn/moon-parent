package dev.t4yrn.moon.bukkit.command.command;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@Getter
@Setter
public class HelpService {

    private final CommandService commandService;
    private HelpFormatter helpFormatter;

    public HelpService(CommandService commandService) {
        this.commandService = commandService;
        this.helpFormatter = (sender, container) -> {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------------------"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUsage &7- &6/" + container.getName()));
            for (cCommand c : container.getCommands().values()) {
                TextComponent msg = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&',
                        "&7/" + container.getName() + (c.getName().length() > 0 ? " &e" + c.getName() : "") + " &7" + c.getMostApplicableUsage() + " &7- &f" + c.getShortDescription()));
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GRAY + "/" + container.getName() + " " + c.getName() + " - " + ChatColor.WHITE + c.getDescription())));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + container.getName() + " " + c.getName()));
                sender.sendMessage(msg);
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m--------------------------------"));
        };
    }

    public void sendHelpFor(CommandSender sender, CommandContainer container) {
        this.helpFormatter.sendHelpFor(sender, container);
    }

    public void sendUsageMessage(CommandSender sender, CommandContainer container, cCommand cCommand) {
        sender.sendMessage(getUsageMessage(container, cCommand));
    }

    public String getUsageMessage(CommandContainer container, cCommand cCommand) {
        String usage = ChatColor.RED + "Command Usage: /" + container.getName() + " ";
        if (cCommand.getName().length() > 0) {
            usage += cCommand.getName() + " ";
        }
        if (cCommand.getUsage() != null && cCommand.getUsage().length() > 0) {
            usage += cCommand.getUsage();
        } else {
            usage += cCommand.getGeneratedUsage();
        }
        return usage;
    }

}
