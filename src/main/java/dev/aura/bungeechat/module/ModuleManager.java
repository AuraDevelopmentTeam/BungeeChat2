package dev.aura.bungeechat.module;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ModuleManager {
    private static List<Module> activeModules = null;

    public static List<Module> getModules() {
        List<Module> modules = new LinkedList<>();

        // Add all modules here!

        return modules;
    }

    public static List<Module> getActiveModules() {
        if (activeModules == null) {
            activeModules = getModules().stream().filter(Module::isEnabled).collect(Collectors.toList());
        }

        return activeModules;
    }

    public static Stream<Module> getActiveModulesStream() {
        return getActiveModules().stream();
    }

    public static void enableModules() {
        getActiveModulesStream().forEach(Module::onEnable);
    }

    public static void disableModules() {
        getActiveModulesStream().forEach(Module::onDisable);
    }

    public static String getActiveModuleString() {
        return getActiveModulesStream().map(Module::getName).collect(Collectors.joining(", "));
    }
}
