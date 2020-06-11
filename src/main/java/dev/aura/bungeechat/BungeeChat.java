package dev.aura.bungeechat;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import dev.aura.bungeechat.account.AccountFileStorage;
import dev.aura.bungeechat.account.AccountSQLStorage;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;
import dev.aura.bungeechat.command.BungeeChatCommand;
import dev.aura.bungeechat.config.Configuration;
import dev.aura.bungeechat.hook.DefaultHook;
import dev.aura.bungeechat.hook.StoredDataHook;
import dev.aura.bungeechat.hook.metrics.MetricManager;
import dev.aura.bungeechat.listener.BungeeChatEventsListener;
import dev.aura.bungeechat.listener.ChannelTypeCorrectorListener;
import dev.aura.bungeechat.listener.CommandTabCompleteListener;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.message.PlaceHolders;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.packet.IdMapping;
import dev.aura.bungeechat.packet.NamedSoundEffectOutPacket;
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.bungeechat.util.MapUtils;
import dev.aura.bungeechat.util.ServerNameUtil;
import dev.aura.lib.version.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;

public class BungeeChat extends Plugin implements BungeeChatApi {
  private static final String storedDataHookName = "storedData";
  private static final String defaultHookName = "default";
  private static final String errorVersion = "error";

  @Getter
  @Setter(AccessLevel.PRIVATE)
  @VisibleForTesting
  static BungeeChat instance;

  private String latestVersion = null;
  private File configDir;
  private File langDir;
  private BungeeChatCommand bungeeChatCommand;
  private BungeecordAccountManager bungeecordAccountManager;
  private ChannelTypeCorrectorListener channelTypeCorrectorListener;
  private BungeeChatEventsListener bungeeChatEventsListener;
  private CommandTabCompleteListener commandTabCompleteListener;

  public BungeeChat() {
    super();
  }

  /** For unit tests only! */
  protected BungeeChat(ProxyServer proxy, PluginDescription description) {
    super(proxy, description);
  }

  @Override
  public void onLoad() {
    setInstance(this);
    BungeeChatInstaceHolder.setInstance(instance);
  }

  @Override
  public void onEnable() {
    onEnable(true);
  }

  public void onEnable(boolean prinLoadScreen) {
    Configuration.load();
    PlaceHolderUtil.loadConfigSections();

    PlaceHolders.registerPlaceHolders();

    final Config accountDatabase = Configuration.get().getConfig("AccountDatabase");
    final Config databaseCredentials = accountDatabase.getConfig("credentials");
    final Config connectionProperties = accountDatabase.getConfig("properties");
    final ImmutableMap<String, String> connectionPropertiesMap =
        connectionProperties.entrySet().stream()
            .collect(
                MapUtils.immutableMapCollector(
                    Map.Entry::getKey, entry -> entry.getValue().unwrapped().toString()));

    if (accountDatabase.getBoolean("enabled")) {
      try {
        AccountManager.setAccountStorage(
            new AccountSQLStorage(
                databaseCredentials.getString("ip"),
                databaseCredentials.getInt("port"),
                databaseCredentials.getString("database"),
                databaseCredentials.getString("user"),
                databaseCredentials.getString("password"),
                databaseCredentials.getString("tablePrefix"),
                connectionPropertiesMap));
      } catch (SQLException e) {
        LoggerHelper.error("Could not connect to specified database. Using file storage", e);

        AccountManager.setAccountStorage(new AccountFileStorage());
      }
    } else {
      AccountManager.setAccountStorage(new AccountFileStorage());
    }

    bungeeChatCommand = new BungeeChatCommand();
    bungeecordAccountManager = new BungeecordAccountManager();
    channelTypeCorrectorListener = new ChannelTypeCorrectorListener();
    bungeeChatEventsListener = new BungeeChatEventsListener();
    commandTabCompleteListener = new CommandTabCompleteListener();

    ProxyServer.getInstance().getPluginManager().registerCommand(this, bungeeChatCommand);
    ProxyServer.getInstance().getPluginManager().registerListener(this, bungeecordAccountManager);
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, channelTypeCorrectorListener);
    ProxyServer.getInstance().getPluginManager().registerListener(this, bungeeChatEventsListener);
    ProxyServer.getInstance().getPluginManager().registerListener(this, commandTabCompleteListener);

    registerPacket(Protocol.GAME, "TO_CLIENT", NamedSoundEffectOutPacket.class);

    Config prefixDefaults = Configuration.get().getConfig("PrefixSuffixSettings");

    BungeecordModuleManager.registerPluginModules();
    ModuleManager.enableModules();
    HookManager.addHook(storedDataHookName, new StoredDataHook());
    HookManager.addHook(
        defaultHookName,
        new DefaultHook(
            prefixDefaults.getString("defaultPrefix"), prefixDefaults.getString("defaultSuffix")));
    ServerNameUtil.loadAliases();

    // Refresh Cache and cache version
    getLatestVersion(true);

    if (prinLoadScreen) {
      MetricManager.sendMetrics(this);

      loadScreen();
    }

    // Finally initialize BungeeChat command map
    commandTabCompleteListener.updateBungeeChatCommands();
  }

  @Override
  public void onDisable() {
    HookManager.removeHook(defaultHookName);
    HookManager.removeHook(storedDataHookName);
    ModuleManager.disableModules();

    ProxyServer.getInstance().getPluginManager().unregisterListener(bungeecordAccountManager);
    ProxyServer.getInstance().getPluginManager().unregisterCommand(bungeeChatCommand);
    ProxyServer.getInstance().getPluginManager().unregisterListener(channelTypeCorrectorListener);
    ProxyServer.getInstance().getPluginManager().unregisterListener(bungeeChatEventsListener);

    ProxyServer.getInstance().getScheduler().cancel(this);

    // Just to be sure
    ProxyServer.getInstance().getPluginManager().unregisterListeners(this);
    ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

    PlaceHolderManager.clear();
    PlaceHolderUtil.clearConfigSections();
    ModuleManager.clearActiveModules();
  }

  @Override
  public File getConfigFolder() {
    if (configDir == null) {
      configDir = new File(getProxy().getPluginsFolder(), "BungeeChat");

      if (!configDir.exists() && !configDir.mkdirs())
        throw new RuntimeException(new IOException("Could not create " + configDir));
    }

    return configDir;
  }

  public File getLangFolder() {
    if (langDir == null) {
      langDir = new File(getConfigFolder(), "lang");

      if (!langDir.exists() && !langDir.mkdirs())
        throw new RuntimeException(new IOException("Could not create " + langDir));
    }

    return langDir;
  }

  @Override
  public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
    MessagesService.sendPrivateMessage(context);
  }

  @Override
  public void sendChannelMessage(BungeeChatContext context, ChannelType channel)
      throws InvalidContextError {
    MessagesService.sendChannelMessage(context, channel);
  }

  private void loadScreen() {
    StartupBannerSize size =
        StartupBannerSize.optionalValueOf(
                Configuration.get().getString("Miscellaneous.startupBannerSize"))
            .orElse(StartupBannerSize.NORMAL);

    if (size == StartupBannerSize.NONE) return;

    if (size != StartupBannerSize.SHORT) {
      LoggerHelper.info(
          ChatColor.GOLD
              + "---------------- "
              + ChatColor.AQUA
              + "Bungee Chat"
              + ChatColor.GOLD
              + " ----------------");
      LoggerHelper.info(getPeopleMessage("Authors", BungeeChatApi.AUTHORS));
    }

    LoggerHelper.info(ChatColor.YELLOW + "Version: " + ChatColor.GREEN + VERSION_STR);

    if (size == StartupBannerSize.LONG) {
      LoggerHelper.info(ChatColor.YELLOW + "Modules:");

      ModuleManager.getAvailableModulesStream()
          .map(
              module -> {
                if (module.isEnabled()) return "\t" + ChatColor.GREEN + "On  - " + module.getName();
                else return "\t" + ChatColor.RED + "Off - " + module.getName();
              })
          .forEachOrdered(LoggerHelper::info);
    } else {
      LoggerHelper.info(
          ChatColor.YELLOW
              + "Modules: "
              + ChatColor.GREEN
              + BungeecordModuleManager.getActiveModuleString());
    }

    if (size != StartupBannerSize.SHORT) {
      LoggerHelper.info(getPeopleMessage("Contributors", BungeeChatApi.CONTRIBUTORS));
      LoggerHelper.info(getPeopleMessage("Translators", BungeeChatApi.TRANSLATORS));
      LoggerHelper.info(getPeopleMessage("Donators", BungeeChatApi.DONATORS));
    }

    if (!isLatestVersion()) {
      LoggerHelper.info(
          ChatColor.YELLOW
              + "There is an update avalible. You can download version "
              + ChatColor.GREEN
              + getLatestVersion()
              + ChatColor.YELLOW
              + " on the plugin page at "
              + URL
              + " !");
    }

    if (size != StartupBannerSize.SHORT) {
      LoggerHelper.info(ChatColor.GOLD + "---------------------------------------------");
    }
  }

  private String getPeopleMessage(String name, String... people) {
    return Arrays.stream(people)
        .collect(
            Collectors.joining(
                BungeecordModuleManager.MODULE_CONCATENATOR,
                ChatColor.YELLOW + name + ": " + ChatColor.GREEN,
                ""));
  }

  private String queryLatestVersion() {
    if (!Configuration.get().getBoolean("Miscellaneous.checkForUpdates")) return VERSION_STR;

    try {
      @Cleanup("disconnect")
      HttpsURLConnection con =
          (HttpsURLConnection)
              new URL("https://api.spigotmc.org/legacy/update.php?resource=" + PLUGIN_ID)
                  .openConnection();
      con.setDoOutput(true);
      con.setRequestMethod("GET");

      con.connect();

      int responseCode = con.getResponseCode();

      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        if (responseCode != 200) {
          LoggerHelper.warning(
              "Invalid response! HTTP code: "
                  + responseCode
                  + " Content:\n"
                  + reader.lines().collect(Collectors.joining("\n")));

          return errorVersion;
        }

        return Optional.ofNullable(reader.readLine()).orElse(errorVersion);
      }
    } catch (Exception ex) {
      LoggerHelper.warning("Could not fetch the latest version!", ex);

      return errorVersion;
    }
  }

  public String getLatestVersion() {
    return getLatestVersion(false);
  }

  public String getLatestVersion(boolean refreshCache) {
    if (refreshCache || (latestVersion == null)) {
      latestVersion = queryLatestVersion();
    }

    return latestVersion;
  }

  public boolean isLatestVersion() {
    return VERSION.compareTo(new Version(getLatestVersion())) >= 0;
  }

  /**
   * Taken from
   * https://github.com/Phoenix616/ResourcepacksPlugins/blob/d765867b9c8d758995907873c2af43d6ec75e1c0/bungee/src/main/java/de/themoep/resourcepacksplugin/bungee/BungeeResourcepacks.java#L170
   */
  @SuppressWarnings("unchecked")
  protected boolean registerPacket(
      Protocol protocol, String directionName, Class<? extends DefinedPacket> packetClass) {
    try {
      Field directionField = Protocol.class.getDeclaredField(directionName);
      directionField.setAccessible(true);

      Object direction = directionField.get(protocol);
      List<Integer> supportedVersions = new ArrayList<>();

      try {
        Field svField = Protocol.class.getField("supportedVersions");
        supportedVersions = (List<Integer>) svField.get(null);
      } catch (Exception e1) {
        // Old bungee protocol version, try new one
      }

      if (supportedVersions.size() == 0) {
        Field svIdField = ProtocolConstants.class.getField("SUPPORTED_VERSION_IDS");
        supportedVersions = (List<Integer>) svIdField.get(null);
      }

      Field field = packetClass.getField("ID_MAPPINGS");

      if (field == null) {
        LoggerHelper.error(packetClass.getSimpleName() + " does not contain ID_MAPPINGS field!");
        return false;
      }

      LoggerHelper.info("Registering " + packetClass.getSimpleName() + "...");

      List<IdMapping> idMappings = (List<IdMapping>) field.get(null);
      final int bungeeVersion = supportedVersions.get(supportedVersions.size() - 1);

      if (bungeeVersion == ProtocolConstants.MINECRAFT_1_8) {
        LoggerHelper.info("BungeeCord 1.8 (" + bungeeVersion + ") detected!");

        Method reg =
            direction.getClass().getDeclaredMethod("registerPacket", int.class, Class.class);
        int id = -1;

        reg.setAccessible(true);

        for (IdMapping mapping : idMappings) {
          if (mapping.getProtocolVersion() == ProtocolConstants.MINECRAFT_1_8) {
            id = mapping.getPacketId();
            break;
          }
        }

        if (id == -1) {
          LoggerHelper.error(packetClass.getSimpleName() + " does not contain an ID for 1.8!");
          return false;
        }

        reg.invoke(direction, id, packetClass);
      } else if (bungeeVersion >= ProtocolConstants.MINECRAFT_1_9
          && bungeeVersion < ProtocolConstants.MINECRAFT_1_9_4) {
        LoggerHelper.info("BungeeCord 1.9-1.9.3 (" + bungeeVersion + ") detected!");

        Method reg =
            direction
                .getClass()
                .getDeclaredMethod("registerPacket", int.class, int.class, Class.class);
        int id18 = -1;
        int id19 = -1;

        reg.setAccessible(true);

        for (IdMapping mapping : idMappings) {
          if (mapping.getProtocolVersion() == ProtocolConstants.MINECRAFT_1_8) {
            id18 = mapping.getPacketId();
          } else if (mapping.getProtocolVersion() >= ProtocolConstants.MINECRAFT_1_9
              && mapping.getProtocolVersion() < ProtocolConstants.MINECRAFT_1_9_4) {
            id19 = mapping.getPacketId();
          }
        }

        if (id18 == -1 || id19 == -1) {
          LoggerHelper.error(
              packetClass.getSimpleName() + " does not contain an ID for 1.8 or 1.9!");
          return false;
        }

        reg.invoke(direction, id18, id19, packetClass);
      } else if (bungeeVersion >= ProtocolConstants.MINECRAFT_1_9_4) {
        LoggerHelper.info("BungeeCord 1.9.4+ (" + bungeeVersion + ") detected!");

        Method map = Protocol.class.getDeclaredMethod("map", int.class, int.class);
        Map<String, Object> mappings = new LinkedHashMap<>();
        ArrayDeque<IdMapping> additionalMappings = new ArrayDeque<>();
        Set<Integer> registeredVersions = new HashSet<>();

        map.setAccessible(true);

        for (IdMapping mapping : idMappings) {
          if (ProtocolConstants.SUPPORTED_VERSION_IDS.contains(mapping.getProtocolVersion())) {
            mappings.put(
                mapping.getName(),
                map.invoke(null, mapping.getProtocolVersion(), mapping.getPacketId()));
            registeredVersions.add(mapping.getProtocolVersion());
          } else {
            additionalMappings.addFirst(mapping);
          }
        }

        // Check if we have a supported version after the additional mapping's id
        // This allows specifying the snapshot version an ID was first used
        for (IdMapping mapping : additionalMappings) {
          for (int id : ProtocolConstants.SUPPORTED_VERSION_IDS) {
            if (!registeredVersions.contains(id) && id > mapping.getProtocolVersion()) {
              LoggerHelper.info(
                  "Using unregistered mapping "
                      + mapping.getName()
                      + "/"
                      + mapping.getProtocolVersion()
                      + " for unregistered version "
                      + id);
              mappings.put(mapping.getName(), map.invoke(null, id, mapping.getPacketId()));
              registeredVersions.add(id);
              break;
            }
          }
        }

        Object mappingsObject =
            Array.newInstance(mappings.values().iterator().next().getClass(), mappings.size());
        int i = 0;

        for (Iterator<Map.Entry<String, Object>> it = mappings.entrySet().iterator();
            it.hasNext();
            i++) {
          Map.Entry<String, Object> entry = it.next();
          Array.set(mappingsObject, i, entry.getValue());

          LoggerHelper.info("Found mapping for " + entry.getKey() + "+");
        }

        Object[] mappingsArray = (Object[]) mappingsObject;
        Method reg =
            direction
                .getClass()
                .getDeclaredMethod("registerPacket", Class.class, mappingsArray.getClass());

        reg.setAccessible(true);

        try {
          reg.invoke(direction, packetClass, mappingsArray);
        } catch (Throwable t) {
          LoggerHelper.error(
              "Protocol version " + bungeeVersion + " is not supported! Please look for an update!",
              t);
          return false;
        }
      } else {
        LoggerHelper.error(
            "Unsupported BungeeCord version ("
                + bungeeVersion
                + ") found! You need at least 1.8 for this plugin to work!");
        return false;
      }

      return true;
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      LoggerHelper.error(
          "Couldn't find a required method! Please update this plugin or downgrade BungeeCord!", e);
    } catch (NoSuchFieldException e) {
      LoggerHelper.error(
          "Couldn't find a required field! Please update this plugin or downgrade BungeeCord!", e);
    }

    return false;
  }

  private enum StartupBannerSize {
    NONE,
    SHORT,
    NORMAL,
    LONG;

    public static Optional<StartupBannerSize> optionalValueOf(String value) {
      for (StartupBannerSize element : values()) {
        if (element.name().equalsIgnoreCase(value)) return Optional.of(element);
      }

      return Optional.empty();
    }
  }
}
