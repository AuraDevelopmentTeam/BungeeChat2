package dev.aura.bungeechat.module;

import java.util.Arrays;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.module.perms.BungeePermsModule;
import dev.aura.bungeechat.module.perms.LuckPermsModule;
import dev.aura.bungeechat.module.perms.PowerfulPermsModule;
import net.md_5.bungee.api.ChatColor;

public class BungeecordModuleManager extends ModuleManager {
    // PermissionHookModules
    public static final BungeePermsModule BUNGEE_PERMS_MODULE = new BungeePermsModule();
    public static final LuckPermsModule LUCK_PERMS_MODULE = new LuckPermsModule();
    public static final PowerfulPermsModule POWERFUL_PERMS_MODULE = new PowerfulPermsModule();

    // Normal Modules
    public static final AlertModule ALERT_MODULE = new AlertModule();
    public static final AntiAdvertisingModule ANTI_ADVERTISING_MODULE = new AntiAdvertisingModule();
    public static final AntiDuplicationModule ANTI_DUPLICATION_MODULE = new AntiDuplicationModule();
    public static final AntiSwearModule ANTI_SWEAR_MODULE = new AntiSwearModule();
    public static final ChatLockModule CHAT_LOCK_MODULE = new ChatLockModule();
    public static final ChatLoggingModule CHAT_LOGGING_MODULE = new ChatLoggingModule();
    public static final GlobalChatModule GLOBAL_CHAT_MODULE = new GlobalChatModule();
    public static final HelpOpModule HELP_OP_MODULE = new HelpOpModule();
    public static final JoinMessageModule JOIN_MESSAGE_MODULE = new JoinMessageModule();
    public static final LeaveMessageModule LEAVE_MESSAGE_MODULE = new LeaveMessageModule();
    public static final ServerSwitchModule SERVER_SWITCH_MODULE = new ServerSwitchModule();
    public static final LocalChatModule LOCAL_CHAT_MODULE = new LocalChatModule();
    public static final MessengerModule MESSENGER_MODULE = new MessengerModule();
    public static final SocialSpyModule SOCIAL_SPY_MODULE = new SocialSpyModule();
    public static final StaffChatModule STAFF_CHAT_MODULE = new StaffChatModule();
    public static final TabCompletionModule TAB_COMPLETION_MODULE = new TabCompletionModule();
    public static final VanishModule VANISHER_MODULE = new VanishModule();

    private static String MODULE_CONCATENATOR = ChatColor.WHITE + ", " + ChatColor.GREEN;
    private static boolean modulesAdded = false;

    public static void registerPluginModules() {
        if (!modulesAdded) {
            availableModules.addAll(0, Arrays.stream(BungeecordModuleManager.class.getDeclaredFields())
                    .filter(field -> BungeeChatModule.class.isAssignableFrom(field.getType())).map(field -> {
                        try {
                            return (BungeeChatModule) field.get(null);
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            e.printStackTrace();

                            return null;
                        }
                    }).collect(Collectors.toList()));

            modulesAdded = true;
        }
    }

    public static String getActiveModuleString() {
        return getActiveModulesStream().map(BungeeChatModule::getName).collect(Collectors.joining(MODULE_CONCATENATOR));
    }
}
