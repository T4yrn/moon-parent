package dev.t4yrn.moon.bukkit.command.parametric;

import dev.t4yrn.moon.bukkit.command.command.cCommand;
import dev.t4yrn.moon.bukkit.command.command.CommandService;
import dev.t4yrn.moon.bukkit.command.exception.CommandStructureException;
import dev.t4yrn.moon.bukkit.command.exception.MissingProviderException;

public class ProviderAssigner {

	private final CommandService commandService;

	public ProviderAssigner(CommandService commandService) {
		this.commandService = commandService;
	}

	public ParameterType<?>[] assignProvidersFor(cCommand cCommand) throws MissingProviderException, CommandStructureException {
		CommandParameters parameters = cCommand.getParameters();
		ParameterType<?>[] providers = new ParameterType<?>[parameters.getParameters().length];
		for (int i = 0; i < parameters.getParameters().length; i++) {

			CommandParameter param = parameters.getParameters()[i];
			if (param.isRequireLastArg() && !parameters.isLastArgument(i)) {
				throw new CommandStructureException("Parameter " + param.getParameter().getName() + " [argument " + i + "] (" + param.getParameter().getType().getSimpleName() + ") in method '" + cCommand.getMethod().getName() + "' must be the last argument in the method.");
			}

			BindingContainer<?> bindings = commandService.getBindingsFor(param.getType());
			if (bindings != null) {
				ParameterType<?> provider = null;
				for (Binding<?> binding : bindings.getBindings()) {
					if (binding.canProvideFor(param)) {
						provider = binding.getProvider();
						break;
					}
				}
				if (provider != null) {
					providers[i] = provider;
				} else {
					throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName() + " for parameter " + i + " for method " + cCommand.getMethod().getName());
				}
			} else {
				throw new MissingProviderException("No provider bound for " + param.getType().getSimpleName());
			}
		}
		return providers;
	}

}
