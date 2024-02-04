package dev.t4yrn.moon.bukkit.command.modifier;

import dev.t4yrn.moon.bukkit.command.command.CommandExecution;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.CommandParameter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public interface CommandModifier<T> {

    Optional<T> modify(@Nonnull CommandExecution execution, @Nonnull CommandParameter commandParameter, @Nullable T argument) throws CommandExitMessage;

}
