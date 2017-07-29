package dev.aura.bungeechat.api.module;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleManager {
    protected static List<BungeeChatModule> availableModules = new LinkedList<>();
    protected static List<BungeeChatModule> activeModules = null;

    public static void registerModule(BungeeChatModule module) throws UnsupportedOperationException {
        availableModules.add(module);
    }

    public static List<BungeeChatModule> getActiveModules() throws UnsupportedOperationException {
        if (activeModules == null) {
            activeModules = availableModules.stream().filter(BungeeChatModule::isEnabled).collect(Collectors.toList());
        }

        return activeModules;
    }

    public static boolean isModuleActive(BungeeChatModule module) throws UnsupportedOperationException {
        return getActiveModules().contains(module);
    }

    public static Stream<BungeeChatModule> getActiveModulesStream() throws UnsupportedOperationException {
        return getActiveModules().stream();
    }

    public static void enableModules() throws UnsupportedOperationException {
        getActiveModulesStream().forEach(BungeeChatModule::onEnable);
    }

    public static void disableModules() throws UnsupportedOperationException {
        getActiveModulesStream().forEach(BungeeChatModule::onDisable);
    }

    public static void clearActiveModules() throws UnsupportedOperationException {
        activeModules = null;
    }
}
