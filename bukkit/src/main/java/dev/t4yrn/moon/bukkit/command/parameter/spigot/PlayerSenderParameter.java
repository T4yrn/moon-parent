package dev.t4yrn.moon.bukkit.command.parameter.spigot;

import dev.t4yrn.moon.bukkit.command.argument.CommandArg;
import dev.t4yrn.moon.bukkit.command.exception.CommandExitMessage;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

public class PlayerSenderParameter extends ParameterType<Player> {

    public static final PlayerSenderParameter INSTANCE = new PlayerSenderParameter();

    @Override
    public boolean doesConsumeArgument() {
        return false;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @Nullable
    public Player provide(@Nonnull CommandArg arg, @Nonnull List<? extends Annotation> annotations) throws CommandExitMessage {
        if (arg.isSenderPlayer()) {
            return arg.getSenderAsPlayer();
        }
        throw new CommandExitMessage("This is a player-only command.");
    }

    @Override
    public String argumentDescription() {
        return "player sender";
    }

}
