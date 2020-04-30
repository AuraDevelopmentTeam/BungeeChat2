package dev.aura.bungeechat.module;

import dev.aura.bungeechat.api.module.BungeeChatModule;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.module.perms.BungeePermsModule;
import dev.aura.bungeechat.module.perms.CloudNetPerms2Module;
import dev.aura.bungeechat.module.perms.CloudNetPerms3Module;
import dev.aura.bungeechat.module.perms.LuckPerms4Module;
import dev.aura.bungeechat.module.perms.LuckPerms5Module;
import dev.aura.bungeechat.module.perms.PowerfulPermsModule;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;

public class BungeecordModuleManager extends ModuleManager {
  // PermissionHookModules
  public static final BungeePermsModule BUNGEE_PERMS_MODULE = new BungeePermsModule();
  public static final CloudNetPerms2Module CLOUD_NET_PERMS2_MODULE = new CloudNetPerms2Module();
  public static final CloudNetPerms3Module CLOUD_NET_PERMS3_MODULE = new CloudNetPerms3Module();
  public static final LuckPerms5Module LUCK_PERMS_5_MODULE = new LuckPerms5Module();
  public static final LuckPerms4Module LUCK_PERMS_4_MODULE = new LuckPerms4Module();
  public static final PowerfulPermsModule POWERFUL_PERMS_MODULE = new PowerfulPermsModule();

  // Normal Modules
  public static final AlertModule ALERT_MODULE = new AlertModule();
  public static final AntiAdvertisingModule ANTI_ADVERTISING_MODULE = new AntiAdvertisingModule();
  public static final AntiDuplicationModule ANTI_DUPLICATION_MODULE = new AntiDuplicationModule();
  public static final AntiSwearModule ANTI_SWEAR_MODULE = new AntiSwearModule();
  public static final AutoBroadcastModule AUTO_BROADCAST_MODULE = new AutoBroadcastModule();
  public static final ChatLockModule CHAT_LOCK_MODULE = new ChatLockModule();
  public static final ChatLoggingModule CHAT_LOGGING_MODULE = new ChatLoggingModule();
  public static final ClearChatModule CLEAR_CHAT_MODULE = new ClearChatModule();
  public static final GlobalChatModule GLOBAL_CHAT_MODULE = new GlobalChatModule();
  public static final HelpOpModule HELP_OP_MODULE = new HelpOpModule();
  public static final IgnoringModule IGNORING_MODULE = new IgnoringModule();
  public static final JoinMessageModule JOIN_MESSAGE_MODULE = new JoinMessageModule();
  public static final LeaveMessageModule LEAVE_MESSAGE_MODULE = new LeaveMessageModule();
  public static final LocalChatModule LOCAL_CHAT_MODULE = new LocalChatModule();
  public static final LocalToModule LOCAL_TO_MODULE = new LocalToModule();
  public static final MessengerModule MESSENGER_MODULE = new MessengerModule();
  public static final MulticastChatModule MULTICAST_CHAT_MODULE = new MulticastChatModule();
  public static final MOTDModule MOTD_MODULE = new MOTDModule();
  public static final MutingModule MUTING_MODULE = new MutingModule();
  public static final ServerSwitchModule SERVER_SWITCH_MODULE = new ServerSwitchModule();
  public static final SpyModule SPY_MODULE = new SpyModule();
  public static final StaffChatModule STAFF_CHAT_MODULE = new StaffChatModule();
  public static final VanishModule VANISHER_MODULE = new VanishModule();
  public static final VersionCheckerModule VERSION_CHECKER_MODULE = new VersionCheckerModule();
  public static final WelcomeMessageModule WELCOME_MESSAGE_MODULE = new WelcomeMessageModule();
  public static final String MODULE_CONCATENATOR = ChatColor.WHITE + ", " + ChatColor.GREEN;

  private static boolean modulesAdded = false;
  private static List<BungeeChatModule> localModules = null;

  public static List<BungeeChatModule> getLocalModules() {
    if (localModules == null) {
      localModules =
          Arrays.stream(BungeecordModuleManager.class.getDeclaredFields())
              .filter(field -> BungeeChatModule.class.isAssignableFrom(field.getType()))
              .map(
                  field -> {
                    try {
                      return (BungeeChatModule) field.get(null);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                      e.printStackTrace();

                      return null;
                    }
                  })
              .collect(Collectors.toList());
    }

    return localModules;
  }

  public static void registerPluginModules() {
    if (!modulesAdded) {
      getAvailableModules().addAll(0, getLocalModules());

      modulesAdded = true;
    }
  }

  public static String getActiveModuleString() {
    return getActiveModulesStream()
        .map(BungeeChatModule::getName)
        .collect(Collectors.joining(MODULE_CONCATENATOR));
  }
}
