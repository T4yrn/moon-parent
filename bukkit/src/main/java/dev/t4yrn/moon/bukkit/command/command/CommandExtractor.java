package dev.t4yrn.moon.bukkit.command.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import dev.t4yrn.moon.bukkit.command.annotation.Require;
import dev.t4yrn.moon.bukkit.command.exception.CommandRegistrationException;
import dev.t4yrn.moon.bukkit.command.exception.CommandStructureException;
import dev.t4yrn.moon.bukkit.command.exception.MissingProviderException;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandExtractor {

	private final CommandService commandService;

	public CommandExtractor(CommandService commandService) {
		this.commandService = commandService;
	}

	public Map<String, cCommand> extractCommands(@Nonnull Object handler) throws MissingProviderException, CommandStructureException {
		Preconditions.checkNotNull(handler, "Handler object cannot be null");
		final Map<String, cCommand> commands = new HashMap<>();
		for (Method method : handler.getClass().getDeclaredMethods()) {
			Optional<cCommand> o = extractCommand(handler, method);
			if (o.isPresent()) {
				cCommand cCommand = o.get();
				commands.put(commandService.getCommandKey(cCommand.getName()), cCommand);
			}
		}
		return commands;
	}

	private Optional<cCommand> extractCommand(@Nonnull Object handler, @Nonnull Method method) throws MissingProviderException, CommandStructureException {
		Preconditions.checkNotNull(handler, "Handler object cannot be null");
		Preconditions.checkNotNull(method, "Method cannot be null");
		if (method.isAnnotationPresent(dev.t4yrn.moon.bukkit.command.annotation.Command.class)) {
			try {
				method.setAccessible(true);
			} catch (SecurityException ex) {
				throw new CommandRegistrationException("Couldn't access method " + method.getName());
			}
			dev.t4yrn.moon.bukkit.command.annotation.Command command = method.getAnnotation(dev.t4yrn.moon.bukkit.command.annotation.Command.class);
			String perm = "";
			if (method.isAnnotationPresent(Require.class)) {
				Require require = method.getAnnotation(Require.class);
				perm = require.value();
			} else if (method.getDeclaringClass().isAnnotationPresent(Require.class)) {
				Require require = method.getDeclaringClass().getAnnotation(Require.class);
				perm = require.value() + (command.name().isEmpty() ? "" : ".") + command.name();
			}
			cCommand cCommand = new cCommand(
					commandService, command.name(), Sets.newHashSet(command.aliases()), command.desc(), command.usage(), command.async(),
					perm, handler, method
			);
			return Optional.of(cCommand);
		}
		return Optional.empty();
	}

}
