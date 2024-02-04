package dev.t4yrn.moon.bukkit.command.parameter.spigot;

import dev.t4yrn.moon.bukkit.command.argument.CommandArg;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import org.bukkit.command.ConsoleCommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class ConsoleCommandSenderParameter extends ParameterType<ConsoleCommandSender> {
    public static final ConsoleCommandSenderParameter INSTANCE = new ConsoleCommandSenderParameter();

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Nullable
    @Override
    public ConsoleCommandSender provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (arg.isSenderPlayer()) {
            throw new CommandExitMessage("This is a console-only command.");
        }

        return null;
    }

    @Override
    public String argumentDescription() {
        return "console sender";
    }

}
