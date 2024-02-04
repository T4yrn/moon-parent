package dev.t4yrn.moon.bukkit.command.parameter;

import dev.t4yrn.moon.bukkit.command.argument.CommandArg;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class DoubleParameter extends ParameterType<Double> {

    public static final DoubleParameter INSTANCE = new DoubleParameter();

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
    public Double defaultNullValue() {
        return 0D;
    }

    @Override
    public Double provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        String s = arg.get();
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException ex) {
            throw new CommandExitMessage("Required: Decimal Number, Given: '" + s + "'");
        }
    }

    @Override
    public String argumentDescription() {
        return "decimal number";
    }
}
