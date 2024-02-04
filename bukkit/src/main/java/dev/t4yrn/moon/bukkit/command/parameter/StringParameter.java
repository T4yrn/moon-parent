package dev.t4yrn.moon.bukkit.command.parameter;

import dev.t4yrn.moon.bukkit.command.argument.CommandArg;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class StringParameter extends ParameterType<String> {

    public static final StringParameter INSTANCE = new StringParameter();

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
        return true;
    }

    @Nullable
    @Override
    public String defaultNullValue() {
        return null;
    }

    @Override
    public String provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        return arg.get();
    }

    @Override
    public String argumentDescription() {
        return "string";
    }

}
