package dev.t4yrn.moon.bukkit.command.parametric;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BindingContainer<T> {

    private final Class<T> type;
    private final Set<Binding<T>> bindings;

    public BindingContainer(Class<T> type) {
        this.type = type;
        this.bindings = new HashSet<>();
    }

    public BindingContainer(Class<T> type, Set<Binding<T>> bindings) {
        this.type = type;
        this.bindings = bindings;
    }

}
