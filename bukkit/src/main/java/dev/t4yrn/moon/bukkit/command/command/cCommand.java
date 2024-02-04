package dev.t4yrn.moon.bukkit.command.command;

import dev.t4yrn.moon.bukkit.command.exception.CommandStructureException;
import dev.t4yrn.moon.bukkit.command.exception.MissingProviderException;
import dev.t4yrn.moon.bukkit.command.parametric.CommandParameter;
import dev.t4yrn.moon.bukkit.command.parametric.CommandParameters;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Set;

@Getter
public class cCommand {

    private final CommandService commandService;
    private final String name;
    private final Set<String> allAliases;
    private final Set<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final Object handler;
    private final Method method;
    private final CommandParameters parameters;
    private final ParameterType<?>[] providers;
    private final ParameterType<?>[] consumingProviders;
    private final int consumingArgCount;
    private final int requiredArgCount;
    private final boolean requiresAsync;
    private final String generatedUsage;

    public cCommand(CommandService commandService, String name, Set<String> aliases, String description, String usage, boolean async, String permission, Object handler, Method method) throws MissingProviderException, CommandStructureException {
        this.commandService = commandService;
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
        this.handler = handler;
        this.method = method;
        this.parameters = new CommandParameters(method);
        this.providers = commandService.getProviderAssigner().assignProvidersFor(this);
        this.consumingArgCount = calculateConsumingArgCount();
        this.requiredArgCount = calculateRequiredArgCount();
        this.consumingProviders = calculateConsumingProviders();
        this.requiresAsync = async || calculateRequiresAsync();
        this.generatedUsage = generateUsage();
        this.allAliases = aliases;
        if (name.length() > 0 && !name.equals(CommandService.DEFAULT_KEY)) {
            allAliases.add(name);
        }
    }

    public String getMostApplicableUsage() {
        if (usage.length() > 0) {
            return usage;
        }
        else {
            return generatedUsage;
        }
    }

    public String getShortDescription() {
        if (description.length() > 24) {
            return description.substring(0, 21) + "...";
        }
        else {
            return description;
        }
    }

    private String generateUsage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter parameter = parameters.getParameters()[i];
            ParameterType provider = providers[i];
            String description = parameter.getParameter().getName(); // provider.argumentDescription()
            if (parameter.isFlag()) {
                sb.append("-").append(parameter.getFlag().value()).append(" ");
            }
            else {
                if (provider.doesConsumeArgument()) {
                    if (parameter.isOptional()) {
                        sb.append("[").append(description);
                        if (parameter.isText()) {
                            sb.append("...");
                        }
                        if (parameter.getDefaultOptionalValue() != null && parameter.getDefaultOptionalValue().length() > 0) {
                            sb.append(" = ").append(parameter.getDefaultOptionalValue());
                        }
                        sb.append("]");
                    } else {
                        sb.append("<").append(description);
                        if (parameter.isText()) {
                            sb.append("...");
                        }
                        sb.append(">");
                    }
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }

    private boolean calculateRequiresAsync() {
        for (ParameterType<?> provider : providers) {
            if (provider.isAsync()) {
                return true;
            }
        }
        return false;
    }

    private ParameterType<?>[] calculateConsumingProviders() {
        ParameterType<?>[] consumingProviders = new ParameterType<?>[consumingArgCount];
        int x = 0;
        for (ParameterType<?> provider : providers) {
            if (provider.doesConsumeArgument()) {
                consumingProviders[x] = provider;
                x++;
            }
        }
        return consumingProviders;
    }

    private int calculateConsumingArgCount() {
        int count = 0;
        for (ParameterType<?> provider : providers) {
            if (provider.doesConsumeArgument()) {
                count++;
            }
        }
        return count;
    }

    private int calculateRequiredArgCount() {
        int count = 0;
        for (int i = 0; i < parameters.getParameters().length; i++) {
            CommandParameter parameter = parameters.getParameters()[i];
            if (!parameter.isFlag() && !parameter.isOptional()) {
                ParameterType provider = providers[i];
                if (provider.doesConsumeArgument()) {
                    count++;
                }
            }
        }
        return count;
    }

}