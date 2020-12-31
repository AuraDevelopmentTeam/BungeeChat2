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
import dev.aura.bungeechat.api.utils.BungeeChatInstanceHolder;
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
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.bungeechat.util.MapUtils;
import dev.aura.bungeechat.util.ServerNameUtil;
import dev.aura.lib.version.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
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
    BungeeChatInstanceHolder.setInstance(instance);
  }

  @Override
  public void onEnable() {
    onEnable(true);
  }

  public void onEnable(boolean printLoadScreen) {
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

    if (printLoadScreen) {
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
              + "There is an update available. You can download version "
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
