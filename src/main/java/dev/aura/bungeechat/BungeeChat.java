package dev.aura.bungeechat;

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
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.message.PlaceHolderUtil;
import dev.aura.bungeechat.message.PlaceHolders;
import dev.aura.bungeechat.message.ServerAliases;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.lib.version.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
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

public class BungeeChat extends Plugin implements BungeeChatApi {
  private static final String storedDataHookName = "storedData";
  private static final String defaultHookName = "default";
  private static final String errorVersion = "error";

  @Getter
  @Setter(AccessLevel.PRIVATE)
  protected static BungeeChat instance;

  private String latestVersion = null;
  private File configDir;
  private File langDir;
  private BungeeChatCommand bungeeChatCommand;
  private BungeecordAccountManager bungeecordAccountManager;
  private ChannelTypeCorrectorListener channelTypeCorrectorListener;
  private BungeeChatEventsListener bungeeChatEventsListener;

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

    Config accountDatabase = Configuration.get().getConfig("AccountDatabase");

    if (accountDatabase.getBoolean("enabled")) {
      try {
        AccountManager.setAccountStorage(
            new AccountSQLStorage(
                accountDatabase.getString("ip"),
                accountDatabase.getInt("port"),
                accountDatabase.getString("database"),
                accountDatabase.getString("user"),
                accountDatabase.getString("password"),
                accountDatabase.getString("tablePrefix")));
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

    ProxyServer.getInstance().getPluginManager().registerCommand(this, bungeeChatCommand);
    ProxyServer.getInstance().getPluginManager().registerListener(this, bungeecordAccountManager);
    ProxyServer.getInstance()
        .getPluginManager()
        .registerListener(this, channelTypeCorrectorListener);
    ProxyServer.getInstance().getPluginManager().registerListener(this, bungeeChatEventsListener);

    Config prefixDefaults = Configuration.get().getConfig("PrefixDefaults");

    BungeecordModuleManager.registerPluginModules();
    ModuleManager.enableModules();
    HookManager.addHook(storedDataHookName, new StoredDataHook());
    HookManager.addHook(
        defaultHookName,
        new DefaultHook(
            prefixDefaults.getString("defaultPrefix"), prefixDefaults.getString("defaultSuffix")));
    ServerAliases.loadAliases();

    // Refresh Cache and cache version
    getLatestVersion(true);

    if (prinLoadScreen) {
      MetricManager.sendMetrics(this);

      loadScreen();
    }
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
      configDir.mkdirs();
    }

    return configDir;
  }

  public File getLangFolder() {
    if (langDir == null) {
      langDir = new File(getConfigFolder(), "lang");
      langDir.mkdirs();
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
      LoggerHelper.info(ChatColor.YELLOW + "Author: " + ChatColor.GREEN + AUTHOR_BRAINSTONE);
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
      LoggerHelper.info(
          ChatColor.YELLOW
              + "Contributors: "
              + ChatColor.GREEN
              + Arrays.stream(BungeeChatApi.CONTRIBUTORS)
                  .collect(Collectors.joining(BungeecordModuleManager.MODULE_CONCATENATOR)));
      LoggerHelper.info(
          ChatColor.YELLOW
              + "Donators: "
              + ChatColor.GREEN
              + Arrays.stream(BungeeChatApi.DONATORS)
                  .collect(Collectors.joining(BungeecordModuleManager.MODULE_CONCATENATOR)));
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

  private String queryLatestVersion() {
    if (!Configuration.get().getBoolean("Miscellaneous.checkForUpdates")) return VERSION_STR;

    try {
      @Cleanup("disconnect")
      HttpsURLConnection con =
          (HttpsURLConnection) new URL("https://www.spigotmc.org/api/general.php").openConnection();
      con.setDoOutput(true);
      con.setRequestMethod("POST");
      con.getOutputStream()
          .write(
              ("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="
                      + PLUGIN_ID)
                  .getBytes(StandardCharsets.UTF_8));

      con.connect();

      int responseCode = con.getResponseCode();
      @Cleanup
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));

      if (responseCode != 200) {
        LoggerHelper.warning(
            "Invalid response! HTTP code: "
                + responseCode
                + " Content:\n"
                + reader.lines().collect(Collectors.joining("\n")));

        return errorVersion;
      }

      return Optional.ofNullable(reader.readLine()).orElse(errorVersion);
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
