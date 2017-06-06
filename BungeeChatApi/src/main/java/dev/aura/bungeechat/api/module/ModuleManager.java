package dev.aura.bungeechat.api.module;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.ServerType;

public class ModuleManager {
    protected static List<BungeeChatModule> availableModules = new LinkedList<>();
    protected static List<BungeeChatModule> activeModules = null;
    private static final boolean validSide = BungeeChatApi.getInstance().getServerType() == ServerType.BUNGEECORD;

    public static void registerModule(BungeeChatModule module) throws UnsupportedOperationException {
        checkSide();

        availableModules.add(module);
    }

    public static List<BungeeChatModule> getActiveModules() throws UnsupportedOperationException {
        checkSide();

        if (activeModules == null) {
            activeModules = availableModules.stream().filter(BungeeChatModule::isEnabled).collect(Collectors.toList());
        }

        return activeModules;
    }

    public static boolean isModuleActive(BungeeChatModule module) throws UnsupportedOperationException {
        checkSide();

        return getActiveModules().contains(module);
    }

    public static Stream<BungeeChatModule> getActiveModulesStream() throws UnsupportedOperationException {
        checkSide();

        return getActiveModules().stream();
    }

    public static void enableModules() throws UnsupportedOperationException {
        checkSide();

        getActiveModulesStream().forEach(BungeeChatModule::onEnable);
    }

    public static void disableModules() throws UnsupportedOperationException {
        checkSide();

        getActiveModulesStream().forEach(BungeeChatModule::onDisable);
    }

    public static void clearActiveModules() throws UnsupportedOperationException {
        checkSide();

        activeModules = null;
    }

    private static void checkSide() throws UnsupportedOperationException {
        if (!validSide)
            throw new UnsupportedOperationException("This operation is only allowed on the BungeeCord!");
    }
}
