package dev.t4yrn.moon.bukkit.command.parametric.binder;

import com.google.common.base.Preconditions;
import dev.t4yrn.moon.bukkit.command.command.CommandService;
import dev.t4yrn.moon.bukkit.command.parametric.ParameterType;
import dev.t4yrn.moon.bukkit.command.parameter.InstanceParameter;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class CommandBinder<T> {

    private final CommandService commandService;
    private final Class<T> type;
    private final Set<Class<? extends Annotation>> classifiers = new HashSet<>();
    private ParameterType<T> provider;

    public CommandBinder(CommandService commandService, Class<T> type) {
        this.commandService = commandService;
        this.type = type;
    }

    public CommandBinder<T> annotatedWith(@Nonnull Class<? extends Annotation> annotation) {
        Preconditions.checkState(commandService.getModifierService().isClassifier(annotation), "Annotation " + annotation.getSimpleName() + " must have @Classifer to be bound");
        classifiers.add(annotation);
        return this;
    }

    public void toInstance(@Nonnull T instance) {
        Preconditions.checkNotNull(instance, "Instance cannot be null for toInstance during binding for " + type.getSimpleName());
        this.provider = new InstanceParameter<>(instance);
        finish();
    }

    public void toProvider(@Nonnull ParameterType<T> provider) {
        Preconditions.checkNotNull(provider, "Provider cannot be null for toProvider during binding for " + type.getSimpleName());
        this.provider = provider;
        finish();
    }

    private void finish() {
        commandService.bindProvider(type, classifiers, provider);
    }

}
