package dev.t4yrn.moon.bukkit.command.command;

import com.google.common.base.Preconditions;
import dev.t4yrn.moon.bukkit.command.annotation.Duration;
import dev.t4yrn.moon.bukkit.command.annotation.Sender;
import dev.t4yrn.moon.bukkit.command.annotation.Text;
import dev.t4yrn.moon.bukkit.command.argument.ArgumentParser;
import dev.t4yrn.moon.bukkit.command.argument.CommandArgs;
import dev.t4yrn.moon.bukkit.command.exception.*;
import dev.t4yrn.moon.bukkit.command.modifier.CommandModifier;
import dev.t4yrn.moon.bukkit.command.modifier.ModifierService;
import dev.t4yrn.moon.bukkit.command.parameter.*;
import dev.t4yrn.moon.bukkit.command.parametric.BindingContainer;
import dev.t4yrn.moon.bukkit.command.parametric.Binding;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import dev.t4yrn.moon.bukkit.command.parametric.ProviderAssigner;
import dev.t4yrn.moon.bukkit.command.parametric.binder.CommandBinder;
import dev.t4yrn.moon.bukkit.command.parameter.spigot.CommandSenderParameter;
import dev.t4yrn.moon.bukkit.command.parameter.spigot.ConsoleCommandSenderParameter;
import dev.t4yrn.moon.bukkit.command.parameter.spigot.PlayerParameter;
import dev.t4yrn.moon.bukkit.command.parameter.spigot.PlayerSenderParameter;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class CommandService implements dev.t4yrn.moon.bukkit.command.CommandService {

    public static String DEFAULT_KEY = "cCOMMAND_DEFAULT";

    private final JavaPlugin plugin;
    private final CommandExtractor extractor;
    private final HelpService helpService;
    private final ProviderAssigner providerAssigner;
    private final ArgumentParser argumentParser;
    private final ModifierService modifierService;
    private final SpigotRegistry spigotRegistry;
    private final FlagExtractor flagExtractor;
    private Authorizer authorizer;

    private final ConcurrentMap<String, CommandContainer> commands = new ConcurrentHashMap<>();
    private final ConcurrentMap<Class<?>, BindingContainer<?>> bindings = new ConcurrentHashMap<>();

    public CommandService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.extractor = new CommandExtractor(this);
        this.helpService = new HelpService(this);
        this.providerAssigner = new ProviderAssigner(this);
        this.argumentParser = new ArgumentParser(this);
        this.modifierService = new ModifierService(this);
        this.spigotRegistry = new SpigotRegistry(this);
        this.flagExtractor = new FlagExtractor(this);
        this.authorizer = new Authorizer();

        this.bindDefaults();
    }

    private void bindDefaults() {
        bind(Boolean.class).toProvider(BooleanParameter.INSTANCE);
        bind(boolean.class).toProvider(BooleanParameter.INSTANCE);
        bind(Double.class).toProvider(DoubleParameter.INSTANCE);
        bind(double.class).toProvider(DoubleParameter.INSTANCE);
        bind(Integer.class).toProvider(IntegerParameter.INSTANCE);
        bind(int.class).toProvider(IntegerParameter.INSTANCE);
        bind(Long.class).toProvider(LongParameter.INSTANCE);
        bind(long.class).toProvider(LongParameter.INSTANCE);
        bind(String.class).toProvider(StringParameter.INSTANCE);
        bind(String.class).annotatedWith(Text.class).toProvider(TextParameter.INSTANCE);
        bind(Date.class).toProvider(DateParameter.INSTANCE);
        bind(Date.class).annotatedWith(Duration.class).toProvider(DurationParameter.INSTANCE);

        bind(CommandArgs.class).toProvider(CommandArgsParameter.INSTANCE);

        bind(CommandSender.class).annotatedWith(Sender.class).toProvider(CommandSenderParameter.INSTANCE);
        bind(ConsoleCommandSender.class).annotatedWith(Sender.class).toProvider(ConsoleCommandSenderParameter.INSTANCE);
        bind(Player.class).annotatedWith(Sender.class).toProvider(PlayerSenderParameter.INSTANCE);
        bind(Player.class).toProvider(new PlayerParameter(plugin));
    }

    @Override
    public void setAuthorizer(@Nonnull Authorizer authorizer) {
        Preconditions.checkNotNull(authorizer, "Authorizer cannot be null");
        this.authorizer = authorizer;
    }

    @Override
    public void registerCommands() {
        commands.values().forEach(cmd -> {
            spigotRegistry.register(cmd, cmd.isOverrideExistingCommands());
        });
    }

    @Override
    public CommandContainer register(@Nonnull Object handler, @Nonnull String name, @Nullable String... aliases) throws CommandRegistrationException {
        Preconditions.checkNotNull(handler, "Handler object cannot be null");
        Preconditions.checkNotNull(name, "Name cannot be null");
        Preconditions.checkState(name.length() > 0, "Name cannot be empty (must be > 0 characters in length)");
        Set<String> aliasesSet = new HashSet<>();
        if (aliases != null) {
            aliasesSet.addAll(Arrays.asList(aliases));
            aliasesSet.removeIf(s -> s.length() == 0);
        }
        try {
            Map<String, cCommand> extractCommands = extractor.extractCommands(handler);
            if (extractCommands.isEmpty()) {
                throw new CommandRegistrationException("There were no commands to register in the " + handler.getClass().getSimpleName() + " class (" + extractCommands.size() + ")");
            }
            CommandContainer container = new CommandContainer(this, handler, name, aliasesSet, extractCommands);
            commands.put(getCommandKey(name), container);
            return container;
        } catch (MissingProviderException | CommandStructureException ex) {
            throw new CommandRegistrationException("Could not register command '" + name + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public CommandContainer registerSub(@Nonnull CommandContainer root, @Nonnull Object handler) {
        Preconditions.checkNotNull(root, "Root command container cannot be null");
        Preconditions.checkNotNull(handler, "Handler object cannot be null");
        try {
            Map<String, cCommand> extractCommands = extractor.extractCommands(handler);
            extractCommands.forEach((s, d) -> root.getCommands().put(s, d));
            return root;
        } catch (MissingProviderException | CommandStructureException ex) {
            throw new CommandRegistrationException("Could not register sub-command in root '" + root + "' with handler '" + handler.getClass().getSimpleName() + "': " + ex.getMessage(), ex);
        }
    }

    @Override
    public <T> void registerModifier(@Nonnull Class<? extends Annotation> annotation, @Nonnull Class<T> type, @Nonnull CommandModifier<T> modifier) {
        modifierService.registerModifier(annotation, type, modifier);
    }

    void executeCommand(@Nonnull CommandSender sender, @Nonnull cCommand cCommand, @Nonnull String label, @Nonnull String[] args) {
        Preconditions.checkNotNull(sender, "Sender cannot be null");
        Preconditions.checkNotNull(cCommand, "Command cannot be null");
        Preconditions.checkNotNull(label, "Label cannot be null");
        Preconditions.checkNotNull(args, "Args cannot be null");
        if (authorizer.isAuthorized(sender, cCommand)) {
            if (cCommand.isRequiresAsync()) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> finishExecution(sender, cCommand, label, args));
            } else {
                finishExecution(sender, cCommand, label, args);
            }
        }
    }

    private void finishExecution(@Nonnull CommandSender sender, @Nonnull cCommand cCommand, @Nonnull String label, @Nonnull String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        try {
            argList = argumentParser.combineMultiWordArguments(argList);
            Map<Character, CommandFlag> flags = flagExtractor.extractFlags(argList);
            final CommandArgs commandArgs = new CommandArgs(this, sender, label, argList, flags);
            CommandExecution execution = new CommandExecution(this, sender, argList, commandArgs, cCommand);
            Object[] parsedArguments = argumentParser.parseArguments(execution, cCommand, commandArgs);
            if (!execution.isCanExecute()) {
                return;
            }
            try {
                cCommand.getMethod().invoke(cCommand.getHandler(), parsedArguments);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                sender.sendMessage(ChatColor.RED + "Could not perform command.  Notify an administrator");
                throw new CommandException("Failed to execute command '" + cCommand.getName() + "' with arguments '" + StringUtils.join(Arrays.asList(args), ' ') + " for sender " + sender.getName(), ex);
            }
        }
        catch (CommandExitMessage ex) {
            ex.print(sender);
        } catch (CommandArgumentException ex) {
            sender.sendMessage(ChatColor.RED + ex.getMessage());
            helpService.sendUsageMessage(sender, getContainerFor(cCommand), cCommand);
        }
    }

    @Nullable
    public CommandContainer getContainerFor(@Nonnull cCommand cCommand) {
        Preconditions.checkNotNull(cCommand, "cCommand cannot be null");
        for (CommandContainer container : commands.values()) {
            if (container.getCommands().containsValue(cCommand)) {
                return container;
            }
        }
        return null;
    }

    @Nullable
    public <T> BindingContainer<T> getBindingsFor(@Nonnull Class<T> type) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        if (bindings.containsKey(type)) {
            return (BindingContainer<T>) bindings.get(type);
        }
        return null;
    }

    @Nullable
    @Override
    public CommandContainer get(@Nonnull String name) {
        Preconditions.checkNotNull(name, "Name cannot be null");
        return commands.get(getCommandKey(name));
    }

    public String getCommandKey(@Nonnull String name) {
        Preconditions.checkNotNull(name, "Name cannot be null");
        if (name.length() == 0) {
            return DEFAULT_KEY;
        }
        return name.toLowerCase();
    }

    @Override
    public <T> CommandBinder<T> bind(@Nonnull Class<T> type) {
        Preconditions.checkNotNull(type, "Type cannot be null for bind");
        return new CommandBinder<>(this, type);
    }

    public <T> void bindProvider(@Nonnull Class<T> type, @Nonnull Set<Class<? extends Annotation>> annotations, @Nonnull ParameterType<T> provider) {
        Preconditions.checkNotNull(type, "Type cannot be null");
        Preconditions.checkNotNull(annotations, "Annotations cannot be null");
        Preconditions.checkNotNull(provider, "Provider cannot be null");
        BindingContainer<T> container = getBindingsFor(type);
        if (container == null) {
            container = new BindingContainer<>(type);
            bindings.put(type, container);
        }
        Binding<T> binding = new Binding<>(type, annotations, provider);
        container.getBindings().add(binding);
    }

}
