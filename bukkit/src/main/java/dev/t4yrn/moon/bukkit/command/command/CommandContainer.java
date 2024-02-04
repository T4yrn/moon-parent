package dev.t4yrn.moon.bukkit.command.command;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CommandContainer extends org.bukkit.command.Command implements PluginIdentifiableCommand {

    private final CommandService commandService;
    private final Object object;
    private final String name;
    private final Set<String> aliases;
    private final Map<String, cCommand> commands;
    private final cCommand defaultCCommand;
    private final CommandExecutor executor;
    private final TabCompleter tabCompleter;
    private boolean overrideExistingCommands = true;
    private boolean defaultCommandIsHelp = false;

    public CommandContainer(CommandService commandService, Object object, String name, Set<String> aliases, Map<String, cCommand> commands) {
        super(name, "", "/" + name, new ArrayList<>(aliases));
        this.commandService = commandService;
        this.object = object;
        this.name = name;
        this.aliases = aliases;
        this.commands = commands;
        this.defaultCCommand = calculateDefaultCommand();
        this.executor = new CommandExecutor(commandService, this);
        this.tabCompleter = new TabCompleter(commandService, this);
        if (defaultCCommand != null) {
            setUsage("/" + name + " " + defaultCCommand.getGeneratedUsage());
            setDescription(defaultCCommand.getDescription());
            setPermission(defaultCCommand.getPermission());
        }
    }

    public final CommandContainer registerSub(@Nonnull Object handler) {
        return commandService.registerSub(this, handler);
    }

    public List<String> getCommandSuggestions(@Nonnull String prefix) {
        Preconditions.checkNotNull(prefix, "Prefix cannot be null");
        final String p = prefix.toLowerCase();
        List<String> suggestions = new ArrayList<>();
        for (cCommand c : commands.values()) {
            for (String alias : c.getAllAliases()) {
                if (alias.length() > 0) {
                    if (p.length() == 0 || alias.toLowerCase().startsWith(p)) {
                        suggestions.add(alias);
                    }
                }
            }
        }
        return suggestions;
    }

    private cCommand calculateDefaultCommand() {
        for (cCommand dc : commands.values()) {
            if (dc.getName().length() == 0 || dc.getName().equals(CommandService.DEFAULT_KEY)) {
                // assume default!
                return dc;
            }
        }
        return null;
    }

    @Nullable
    public cCommand get(@Nonnull String name) {
        Preconditions.checkNotNull(name, "Name cannot be null");
        return commands.get(commandService.getCommandKey(name));
    }

    @Nullable
    public cCommand getByKeyOrAlias(@Nonnull String key) {
        Preconditions.checkNotNull(key, "Key cannot be null");
        if (commands.containsKey(key)) {
            return commands.get(key);
        }
        for (cCommand cCommand : commands.values()) {
            if (cCommand.getAliases().contains(key)) {
                return cCommand;
            }
        }
        return null;
    }

    /**
     * Gets a sub-command based on given arguments and also returns the new actual argument values
     * based on the arguments that were consumed for the sub-command key
     * @param args the original arguments passed in
     * @return the cCommand (if present, Nullable) and the new argument array
     */
    @Nullable
    public Map.Entry<cCommand, String[]> getCommand(String[] args) {
        for (int i = (args.length - 1); i >= 0; i--) {
            String key = commandService.getCommandKey(StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 0, i + 1)), ' '));
            cCommand cCommand = getByKeyOrAlias(key);
            if (cCommand != null) {
                return new AbstractMap.SimpleEntry<>(cCommand, Arrays.copyOfRange(args, i + 1, args.length));
            }
        }
        return new AbstractMap.SimpleEntry<>(getDefaultCommand(), args);
    }

    @Nullable
    public cCommand getDefaultCommand() {
        return defaultCCommand;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        return executor.onCommand(commandSender, this, s, strings);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return tabCompleter.onTabComplete(sender, this, alias, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return tabCompleter.onTabComplete(sender, this, alias, args);
    }

    @Override
    public Plugin getPlugin() {
        return commandService.getPlugin();
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<String> getcCommandAliases() {
        return aliases;
    }

    public Map<String, cCommand> getCommands() {
        return commands;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }

    public boolean isOverrideExistingCommands() {
        return overrideExistingCommands;
    }

    public CommandContainer setOverrideExistingCommands(boolean overrideExistingCommands) {
        this.overrideExistingCommands = overrideExistingCommands;
        return this;
    }

    public boolean isDefaultCommandIsHelp() {
        return defaultCommandIsHelp;
    }

    public CommandContainer setDefaultCommandIsHelp(boolean defaultCommandIsHelp) {
        this.defaultCommandIsHelp = defaultCommandIsHelp;
        return this;
    }
}
