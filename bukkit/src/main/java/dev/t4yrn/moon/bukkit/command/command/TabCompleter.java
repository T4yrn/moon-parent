package dev.t4yrn.moon.bukkit.command.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    private final CommandService commandService;
    private final CommandContainer container;

    public TabCompleter(CommandService commandService, CommandContainer container) {
        this.commandService = commandService;
        this.container = container;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(container.getName())) {
            Map.Entry<cCommand, String[]> data = container.getCommand(args);
            if (data != null && data.getKey() != null) {
                String tabCompleting = "";
                int tabCompletingIndex = 0;
                if (data.getValue().length > 0) {
                    tabCompleting = data.getValue()[data.getValue().length - 1];
                    tabCompletingIndex = data.getValue().length - 1;
                }
                cCommand cCommand = data.getKey();
                if (cCommand.getConsumingProviders().length > tabCompletingIndex) {
                    List<String> s = cCommand.getConsumingProviders()[tabCompletingIndex].getSuggestions(sender, tabCompleting);
                    if (s != null) {
                        List<String> suggestions = new ArrayList<>(s);
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            suggestions.addAll(container.getCommandSuggestions(tC));
                        }
                        return suggestions;
                    }
                    else {
                        if (args.length == 0 || args.length == 1) {
                            String tC = "";
                            if (args.length > 0) {
                                tC = args[args.length - 1];
                            }
                            return container.getCommandSuggestions(tC);
                        }
                    }
                }
                else {
                    if (args.length == 0 || args.length == 1) {
                        String tC = "";
                        if (args.length > 0) {
                            tC = args[args.length - 1];
                        }
                        return container.getCommandSuggestions(tC);
                    }
                }
            }
            else {
                if (args.length == 0 || args.length == 1) {
                    String tC = "";
                    if (args.length > 0) {
                        tC = args[args.length - 1];
                    }
                    return container.getCommandSuggestions(tC);
                }
            }
        }
        return Collections.emptyList();
    }
}
