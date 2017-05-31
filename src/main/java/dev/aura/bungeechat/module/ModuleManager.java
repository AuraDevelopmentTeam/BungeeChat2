package dev.aura.bungeechat.module;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class ModuleManager {
    public static final AlertModule ALERT_MODULE = new AlertModule();
    public static final AntiSwearModule ANTI_SWEAR_MODULE = new AntiSwearModule();
    public static final GlobalChatModule GLOBAL_CHAT_MODULE = new GlobalChatModule();
    public static final HelpOpModule HELP_OP_MODULE = new HelpOpModule();
    public static final MessengerModule MESSENGER_MODULE = new MessengerModule();
    public static final SocialSpyModule SOCIAL_SPY_MODULE = new SocialSpyModule();
    public static final VanisherModule VANISHER_MODULE = new VanisherModule();
    public static final StaffChatModule STAFF_CHAT_MODULE = new StaffChatModule();
    public static final LocalChatModule LOCAL_CHAT_MODULE = new LocalChatModule();
    public static final LeaveMessageModule LEAVE_MESSAGE_MODULE = new LeaveMessageModule();
    public static final JoinMessageModule JOIN_MESSAGE_MODULE = new JoinMessageModule();
    public static final TabCompletionModule TAB_COMPLETION_MODULE = new TabCompletionModule();

    private static List<Module> activeModules = null;
    private static String MODULE_CONCATENATOR = ChatColor.WHITE + ", " + ChatColor.GREEN;

    public static Stream<Module> getModules() {
        return Arrays.stream(ModuleManager.class.getDeclaredFields())
                .filter(field -> Module.class.isAssignableFrom(field.getType())).map(field -> {
                    try {
                        return (Module) field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();

                        return null;
                    }
                });
    }

    public static List<Module> getActiveModules() {
        if (activeModules == null) {
            activeModules = getModules().filter(Module::isEnabled).collect(Collectors.toList());
        }

        return activeModules;
    }

    public static boolean isModuleActive(Module module) {
        return getActiveModules().contains(module);
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
        return getActiveModulesStream().map(Module::getName).collect(Collectors.joining(MODULE_CONCATENATOR));
    }
}
