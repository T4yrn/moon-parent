package dev.t4yrn.moon.bukkit.command;

import com.google.common.base.Preconditions;
import dev.t4yrn.moon.bukkit.command.command.CommandService;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is the main class of CommandAPI
 * CommandAPI can be shaded or used as a plugin
 * This class provides the plugin functionality
 * As well, this class can be used to get an instance of a
 * {@link dev.t4yrn.moon.bukkit.command.CommandService} for your plugin to register commands via.
 */
public class CommandAPI extends JavaPlugin {

    private static final ConcurrentMap<String, dev.t4yrn.moon.bukkit.command.CommandService> services = new ConcurrentHashMap<>();

    /**
     * Get a {@link dev.t4yrn.moon.bukkit.command.CommandService} instance to register commands via
     * - JavaPlugin specific (one per plugin instance)
     *
     * @param javaPlugin {@link Nonnull} your {@link JavaPlugin} instance
     * @return The {@link dev.t4yrn.moon.bukkit.command.CommandService} instance
     */
    public static dev.t4yrn.moon.bukkit.command.CommandService get(@Nonnull JavaPlugin javaPlugin) {
        Preconditions.checkNotNull(javaPlugin, "JavaPlugin cannot be null");
        return services.computeIfAbsent(javaPlugin.getName(), name -> new CommandService(javaPlugin));
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
