package dev.t4yrn.moon.bukkit.command.parameter;

import dev.t4yrn.moon.bukkit.command.argument.CommandArg;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class LongParameter extends ParameterType<Long> {

    public static final LongParameter INSTANCE = new LongParameter();

    @Override
    public boolean doesConsumeArgument() {
        return true;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean allowNullArgument() {
        return false;
    }

    @Nullable
    @Override
    public Long defaultNullValue() {
        return 0L;
    }

    @Override
    public Long provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Long.parseLong(s);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Long Number, Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return "long number";
    }

}
