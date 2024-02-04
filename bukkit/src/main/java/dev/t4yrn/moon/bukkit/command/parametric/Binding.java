package dev.t4yrn.moon.bukkit.command.parametric;

import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Set;

@Getter
public class Binding<T> {

    private final Class<T> type;
    private final Set<Class<? extends Annotation>> annotations;
    private final ParameterType<T> provider;

    public Binding(Class<T> type, Set<Class<? extends Annotation>> annotations, ParameterType<T> provider) {
        this.type = type;
        this.annotations = annotations;
        this.provider = provider;
    }

    public boolean canProvideFor(@Nonnull CommandParameter parameter) {
        Preconditions.checkNotNull(parameter, "Parameter cannot be null");
        // The parameter and binding need to have exact same annotations
        for (Annotation c : parameter.getClassifierAnnotations()) {
            if (!annotations.contains(c.annotationType())) {
                return false;
            }
        }
        for (Class<? extends Annotation> annotation : annotations) {
            if (parameter.getClassifierAnnotations().stream().noneMatch(a -> a.annotationType().equals(annotation))) {
                return false;
            }
        }
        return true;
    }

}
