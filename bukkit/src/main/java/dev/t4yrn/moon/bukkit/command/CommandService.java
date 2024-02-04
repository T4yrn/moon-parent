package dev.t4yrn.moon.bukkit.command;

import dev.t4yrn.moon.bukkit.command.command.Authorizer;
import dev.t4yrn.moon.bukkit.command.command.CommandContainer;
import dev.t4yrn.moon.bukkit.command.modifier.CommandModifier;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import dev.t4yrn.moon.bukkit.command.parametric.binder.CommandBinder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * cCommand CommandServices are {@link org.bukkit.plugin.java.JavaPlugin}-specific.
 * Meaning one per JavaPlugin (Spigot/Bukkit Plugin).
 * Since the commands are associated with the specified command,
 * and are dynamically registered into the server's CommandMap
 * <p>
 * See {@link CommandAPI#get(JavaPlugin)} for getting an instance of a functional {@link CommandService}
 */
public interface CommandService {

    /**
     * Register a cCommand command into the cCommand Command Service
     * @param handler Object that has the {@link dev.t4yrn.moon.bukkit.command.annotation.Command} annotated methods
     * @param name The name of the command to register.
     *             The names of methods within the handler object will be sub-commands to this name.
     *             If you want to create a default command (just /name), set the name here and in the
     * {@link dev.t4yrn.moon.bukkit.command.annotation.Command} annotation set name = ""
     * @param aliases (Optional) A list of alternate command names that can be used
     * @return The {@link CommandContainer} containing the command you registered
     */
    CommandContainer register(@Nonnull Object handler, @Nonnull String name, @Nullable String... aliases);

    /**
     * Register a sub-command into the specified root command container
     *
     * @param root    The {@link CommandContainer} super-command to register your sub-commands into
     * @param handler The object that has the {@link dev.t4yrn.moon.bukkit.command.annotation.Command}
     *                annotated methods to register
     * @return The {@link CommandContainer} containing the command you registered (same as the root passed in)
     */
    CommandContainer registerSub(@Nonnull CommandContainer root, @Nonnull Object handler);

    /**
     * Must be called after all of you commands have been registered into cCommand with
     * {@link #register(Object, String, String...)} and {@link #registerSub(CommandContainer, Object)}
     *
     * This registers the command into the Bukkit/Spigot CommandMap so that they can be executed on the server.
     */
    void registerCommands();

    /**
     * Start binding a class type to a {@link ParameterType} or instance.
     * @param type The Class type to bind to
     * @param <T> The type of class
     * @return A {@link CommandBinder} instance to finish the binding
     */
    <T> CommandBinder<T> bind(@Nonnull Class<T> type);

    /**
     * Registers a modifier to modify provided arguments for a specific type
     * @param annotation The annotation to use for the modifier (must have {@link dev.t4yrn.moon.bukkit.command.annotation.Modifier} annotated in it's class)
     * @param type The type to modify
     * @param modifier The modifier
     * @param <T> The type of class to modify
     */
    <T> void registerModifier(@Nonnull Class<? extends Annotation> annotation, @Nonnull Class<T> type, @Nonnull CommandModifier<T> modifier);

    /**
     *
     * @param name The primary name of the {@link CommandContainer} you want to get
     * @return {@link Nullable} The {@link CommandContainer} with the specified name
     */
    @Nullable
    CommandContainer get(@Nonnull String name);

    /**
     * Set the authorizer that cCommand uses.
     * This will allow you to edit the behavior for checking if a {@link org.bukkit.command.CommandSender} or
     * {@link org.bukkit.entity.Player} has permission to run a command.
     * You can also edit the no-permission message by modifying this, or use {@link Authorizer#setNoPermissionMessage(String)}
     * @param authorizer {@link Nonnull} A {@link Authorizer} instance to be used for
     *                                  checking authorization for command execution
     */
    void setAuthorizer(@Nonnull Authorizer authorizer);

}
