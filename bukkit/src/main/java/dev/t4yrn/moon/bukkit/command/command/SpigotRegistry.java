package dev.t4yrn.moon.bukkit.command.command;

import dev.t4yrn.moon.bukkit.command.exception.CommandRegistrationException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SpigotRegistry {

    private final CommandService commandService;
    private CommandMap commandMap;

    public SpigotRegistry(CommandService commandService) {
        this.commandService = commandService;
        try {
            commandMap = (CommandMap) getPrivateField(Bukkit.getServer(), "commandMap", false);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Command> getKnownCommands() throws NoSuchFieldException, IllegalAccessException {
        Object map = getPrivateField(commandMap, "knownCommands", true);
        @SuppressWarnings("unchecked")
        HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
        return knownCommands;
    }

    private Object getPrivateField(Object object, String field, boolean fallback) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field objectField;

        try {
            objectField = clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            if(fallback) {
                objectField = clazz.getSuperclass().getDeclaredField(field);
            } else {
                throw new NoSuchFieldException(e.getMessage());
            }
        }

        objectField.setAccessible(true);
        Object result = objectField.get(object);
        objectField.setAccessible(false);
        return result;
    }


    public boolean register(@Nonnull CommandContainer container, boolean unregisterExisting) throws CommandRegistrationException {
        if (unregisterExisting) {
            try {
                Map<String, Command> knownCommands = getKnownCommands();
                if (knownCommands.containsKey(container.getName().toLowerCase())) {
                    knownCommands.remove(container.getName().toLowerCase()).unregister(commandMap);
                }
                for (String s : container.getcCommandAliases()) {
                    if (knownCommands.containsKey(s.toLowerCase())) {
                        knownCommands.remove(s).unregister(commandMap);
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new CommandRegistrationException("Couldn't access knownCommands field in Bukkit CommandMap to unregister existing command(s)");
            }
        }

        return commandMap.register(container.getCommandService().getPlugin().getName(), container);
    }



}
