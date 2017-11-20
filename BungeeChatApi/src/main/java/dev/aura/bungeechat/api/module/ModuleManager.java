package dev.aura.bungeechat.api.module;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public class ModuleManager {
	@Getter
	private static final List<BungeeChatModule> availableModules = new LinkedList<>();
	private static final List<BungeeChatModule> activeModules = new LinkedList<>();

	public static void registerModule(BungeeChatModule module) throws UnsupportedOperationException {
		availableModules.add(module);
	}

	public static List<BungeeChatModule> getActiveModules() throws UnsupportedOperationException {
		if (activeModules.isEmpty()) {
			activeModules
					.addAll(availableModules.stream().filter(BungeeChatModule::isEnabled).collect(Collectors.toList()));
		}

		return activeModules;
	}

	public static boolean isModuleActive(BungeeChatModule module) throws UnsupportedOperationException {
		return getActiveModules().contains(module);
	}

	public static Stream<BungeeChatModule> getAvailableModulesStream() throws UnsupportedOperationException {
		return getAvailableModules().stream();
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
		activeModules.clear();
	}
}
