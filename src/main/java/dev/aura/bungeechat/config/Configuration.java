package dev.aura.bungeechat.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.util.LoggerHelper;
import dev.aura.bungeechat.util.MapUtils;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Configuration implements Config {
    protected static final String CONFIG_FILE_NAME = "config.conf";
    protected static final String OLD_CONFIG_FILE_NAME = "config.yml";
    protected static final String OLD_OLD_CONFIG_FILE_NAME = "config.old.yml";
    protected static final File CONFIG_FILE = new File(BungeeChat.getInstance().getConfigFolder(), CONFIG_FILE_NAME);
    protected static final File OLD_CONFIG_FILE = new File(CONFIG_FILE.getParentFile(), OLD_CONFIG_FILE_NAME);
    protected static final File OLD_OLD_CONFIG_FILE = new File(CONFIG_FILE.getParentFile(), OLD_OLD_CONFIG_FILE_NAME);
    @Getter(value = AccessLevel.PROTECTED, lazy = true)
    private static final String header = loadHeader();

    protected static Configuration currentConfig;

    @Delegate
    protected Config config;

    /**
     * Creates and loads the config. Also saves it so that all missing values
     * exist!<br>
     * Also set currentConfig to this config.
     *
     * @return a configuration object, loaded from the config file.
     */
    public static Configuration load() {
        Configuration config = new Configuration();
        config.loadConfig();

        currentConfig = config;

        return currentConfig;
    }

    public static Configuration get() {
        return currentConfig;
    }

    private static String loadHeader() {
        StringBuilder header = new StringBuilder();

        try {
            @Cleanup
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(BungeeChat.getInstance().getResourceAsStream(CONFIG_FILE_NAME)));
            String line = reader.readLine();

            while (line.startsWith("#")) {
                header.append(line).append('\n');

                line = reader.readLine();
            }
        } catch (IOException e) {
            LoggerHelper.error("Error loading file header", e);
        }

        return header.toString();
    }

    protected void loadConfig() {
        Config defaultConfig = ConfigFactory
                .parseReader(new InputStreamReader(BungeeChat.getInstance().getResourceAsStream(CONFIG_FILE_NAME),
                        StandardCharsets.UTF_8), MessagesTranslator.PARSE_OPTIONS);

        if (CONFIG_FILE.exists()) {
            try {
                Config fileConfig = ConfigFactory.parseFile(CONFIG_FILE, MessagesTranslator.PARSE_OPTIONS);

                config = fileConfig.withFallback(defaultConfig.withoutPath("ServerAlias"));
            } catch (ConfigException e) {
                LoggerHelper.error("Error while reading config:\n" + e.getLocalizedMessage());

                config = defaultConfig;
            }
        } else {
            config = defaultConfig;
        }

        config = config.resolve();

        convertOldConfig();

        saveConfig();
    }

    protected void saveConfig() {
        try {
            @Cleanup
            PrintWriter writer = new PrintWriter(CONFIG_FILE, StandardCharsets.UTF_8.name());
            String renderedConfig = config.root().render(MessagesTranslator.RENDER_OPTIONS);
            renderedConfig = getHeader() + renderedConfig;

            writer.print(renderedConfig);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            LoggerHelper.error("Something very unexpected happend! Please report this!", e);
        }
    }

    private void convertOldConfig() {
        if (OLD_CONFIG_FILE.exists()) {
            convertYAMLConfig();
        }

        switch (String.format(Locale.ROOT, "%.1f", config.getDouble("Version"))) {
        default:
            // Unknow Version or old version
            // -> Update version
            config = config.withValue("Version", ConfigValueFactory.fromAnyRef(BungeeChatApi.CONFIG_VERSION));

        case "11.0":
            // Up to date
            // -> No action needed
        }
    }

    private void convertYAMLConfig() {
        try {
            LoggerHelper.warning("Detected old YAML config. Trying to migrate to HOCON.");

            // Read old config
            @Cleanup
            InputStreamReader reader = new InputStreamReader(new FileInputStream(OLD_CONFIG_FILE),
                    StandardCharsets.UTF_8);
            net.md_5.bungee.config.Configuration oldConfig = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(reader);
            net.md_5.bungee.config.Configuration section;

            // Close file
            reader.close();

            // Migrate settigns
            section = oldConfig.getSection("AccountDataBase");
            final ImmutableMap<String, Object> accountDatabase = ImmutableMap.<String, Object>builder()
                    .put("database", section.getString("database")).put("enabled", section.getBoolean("enabled"))
                    .put("ip", section.getString("ip")).put("password", section.getString("password"))
                    .put("port", section.getInt("port")).put("tablePrefix", section.getString("tablePrefix"))
                    .put("user", section.getString("user")).build();

            section = oldConfig.getSection("Formats");
            final ImmutableMap<String, Object> formats = ImmutableMap.<String, Object>builder()
                    .put("alert", section.getString("alert"))
                    .put("chatLoggingConsole", section.getString("chatLoggingConsole"))
                    .put("chatLoggingFile", section.getString("chatLoggingFile"))
                    .put("globalChat", section.getString("globalChat")).put("helpOp", section.getString("helpOp"))
                    .put("joinMessage", section.getString("joinMessage"))
                    .put("leaveMessage", section.getString("leaveMessage"))
                    .put("localChat", section.getString("localChat")).put("localSpy", section.getString("localSpy"))
                    .put("messageSender", section.getString("messageSender"))
                    .put("messageTarget", section.getString("messageTarget"))
                    .put("motd",
                            oldConfig.getStringList("Settings.Modules.MOTD.message").stream()
                                    .collect(Collectors.joining("\n")))
                    .put("serverSwitch", section.getString("serverSwitch"))
                    .put("socialSpy", section.getString("socialSpy")).put("staffChat", section.getString("staffChat"))
                    .put("welcomeMessage", oldConfig.getStringList("Settings.Modules.WelcomeMessage.message").stream()
                            .collect(Collectors.joining("\n")))
                    .build();

            final net.md_5.bungee.config.Configuration modulesSection = oldConfig.getSection("Settings.Modules");
            section = modulesSection.getSection("Alert");
            final ImmutableMap<String, Object> moduleAlert = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("enabled", section.getBoolean("enabled"))
                    .build();

            section = modulesSection.getSection("AntiAdvertising");
            final ImmutableMap<String, Object> moduleAntiAdvertising = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled"))
                    .put("whitelisted", section.getStringList("whitelisted")).build();

            section = modulesSection.getSection("AntiDuplication");
            final ImmutableMap<String, Object> moduleAntiDuplication = ImmutableMap.<String, Object>builder()
                    .put("checkPastMessages", section.getInt("checkPastMessages"))
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("AntiSwear");
            final ImmutableMap<String, Object> moduleAntiSwear = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled"))
                    .put("freeMatching", section.getBoolean("freeMatching"))
                    .put("ignoreDuplicateLetters", section.getBoolean("ignoreDuplicateLetters"))
                    .put("ignoreSpaces", section.getBoolean("ignoreSpaces"))
                    .put("leetSpeak", section.getBoolean("leetSpeak"))
                    .put("replacement", section.getString("replacement")).put("words", section.getStringList("words"))
                    .build();

            section = modulesSection.getSection("AutoBroadcast");
            final ImmutableMap<String, Object> moduleAutoBroadcast = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).put("interval", section.getInt("interval") + "s")
                    .put("messages", section.getStringList("messages")).put("random", section.getBoolean("random"))
                    .build();

            section = modulesSection.getSection("ChatLock");
            final ImmutableMap<String, Object> moduleChatLock = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases"))
                    .put("emptyLinesOnClear", section.getInt("emptyLinesOnClear"))
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("ChatLogging");
            final ImmutableMap<String, Object> moduleChatLogging = ImmutableMap.<String, Object>builder()
                    .put("console", section.getBoolean("console")).put("enabled", section.getBoolean("enabled"))
                    .put("file", section.getBoolean("file"))
                    .put("filteredCommands", section.getStringList("filteredCommands"))
                    .put("logFile", section.getString("logFile"))
                    .put("privateMessages", section.getBoolean("privateMessages")).build();

            section = modulesSection.getSection("ClearChat");
            final ImmutableMap<String, Object> moduleClearChat = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases"))
                    .put("emptyLinesOnClear", section.getInt("emptyLinesOnClear"))
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("GlobalChat");
            final ImmutableMap<String, Object> moduleGlobalChatServerList = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("serverList.enabled"))
                    .put("serverList", section.getStringList("serverList.serverList")).build();
            final ImmutableMap<String, Object> moduleGlobalChatSymbol = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("symbol.enabled"))
                    .put("symbol", section.getString("symbol.symbol")).build();
            final ImmutableMap<String, Object> moduleGlobalChat = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("default", section.getBoolean("default"))
                    .put("enabled", section.getBoolean("enabled"))
                    .put("passToClientServer", section.getBoolean("passToClientServer"))
                    .put("serverList", moduleGlobalChatServerList).put("symbol", moduleGlobalChatSymbol).build();

            section = modulesSection.getSection("HelpOp");
            final ImmutableMap<String, Object> moduleHelpOp = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("enabled", section.getBoolean("enabled"))
                    .build();

            section = modulesSection.getSection("Ignoring");
            final ImmutableMap<String, Object> moduleIgnoring = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("enabled", section.getBoolean("enabled"))
                    .build();

            section = modulesSection.getSection("JoinMessage");
            final ImmutableMap<String, Object> moduleJoinMessage = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("LeaveMessage");
            final ImmutableMap<String, Object> moduleLeaveMessage = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("LocalChat");
            final ImmutableMap<String, Object> moduleLocalChat = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled"))
                    .put("passToClientServer", section.getBoolean("passToClientServer")).build();

            section = modulesSection.getSection("MOTD");
            final ImmutableMap<String, Object> moduleMOTD = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("Messenger");
            final ImmutableMap<String, Object> moduleMessengerAliases = ImmutableMap.<String, Object>builder()
                    .put("message", section.getStringList("aliases.message"))
                    .put("msgtoggle", section.getStringList("aliases.msgtoggle"))
                    .put("reply", section.getStringList("aliases.reply")).build();
            final ImmutableMap<String, Object> moduleMessenger = ImmutableMap.<String, Object>builder()
                    .put("aliases", moduleMessengerAliases).put("enabled", section.getBoolean("enabled"))
                    .put("filterPrivateMessages", section.getBoolean("filterMessages")).build();

            section = modulesSection.getSection("Muting");
            final ImmutableMap<String, Object> moduleMutingAliases = ImmutableMap.<String, Object>builder()
                    .put("mute", section.getStringList("aliases.mute"))
                    .put("tempmute", section.getStringList("aliases.tempmute"))
                    .put("unmute", section.getStringList("aliases.unmute")).build();
            final ImmutableMap<String, Object> moduleMuting = ImmutableMap.<String, Object>builder()
                    .put("aliases", moduleMutingAliases)
                    .put("blockedcommands", section.getStringList("blockedcommands"))
                    .put("disableWithOtherMutePlugins", section.getBoolean("disableWithOtherMutePlugins"))
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("ServerSwitchMessages");
            final ImmutableMap<String, Object> moduleServerSwitchMessages = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("Spy");
            final ImmutableMap<String, Object> moduleSpyAliases = ImmutableMap.<String, Object>builder()
                    .put("localspy", section.getStringList("aliases.localspy"))
                    .put("socialspy", section.getStringList("aliases.socialspy")).build();
            final ImmutableMap<String, Object> moduleSpy = ImmutableMap.<String, Object>builder()
                    .put("aliases", moduleSpyAliases).put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("StaffChat");
            final ImmutableMap<String, Object> moduleStaffChat = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("enabled", section.getBoolean("enabled"))
                    .put("passToClientServer", section.getBoolean("passToClientServer")).build();

            section = modulesSection.getSection("TabCompletion");
            final ImmutableMap<String, Object> moduleTabCompletion = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("Vanish");
            final ImmutableMap<String, Object> moduleVanish = ImmutableMap.<String, Object>builder()
                    .put("aliases", section.getStringList("aliases")).put("enabled", section.getBoolean("enabled"))
                    .build();

            section = modulesSection.getSection("VersionChecker");
            final ImmutableMap<String, Object> moduleVersionChecker = ImmutableMap.<String, Object>builder()
                    .put("checkOnAdminLogin", section.getBoolean("checkOnAdminLogin"))
                    .put("enabled", section.getBoolean("enabled")).build();

            section = modulesSection.getSection("WelcomeMessage");
            final ImmutableMap<String, Object> moduleWelcomeMessage = ImmutableMap.<String, Object>builder()
                    .put("enabled", section.getBoolean("enabled")).build();

            final ImmutableMap<String, Object> modules = ImmutableMap.<String, Object>builder()
                    .put("Alert", moduleAlert).put("AntiAdvertising", moduleAntiAdvertising)
                    .put("AntiDuplication", moduleAntiDuplication).put("AntiSwear", moduleAntiSwear)
                    .put("AutoBroadcast", moduleAutoBroadcast).put("ChatLock", moduleChatLock)
                    .put("ChatLogging", moduleChatLogging).put("ClearChat", moduleClearChat)
                    .put("GlobalChat", moduleGlobalChat).put("HelpOp", moduleHelpOp).put("Ignoring", moduleIgnoring)
                    .put("JoinMessage", moduleJoinMessage).put("LeaveMessage", moduleLeaveMessage)
                    .put("LocalChat", moduleLocalChat).put("MOTD", moduleMOTD).put("Messenger", moduleMessenger)
                    .put("Muting", moduleMuting).put("ServerSwitchMessages", moduleServerSwitchMessages)
                    .put("Spy", moduleSpy).put("StaffChat", moduleStaffChat).put("TabCompletion", moduleTabCompletion)
                    .put("Vanish", moduleVanish).put("VersionChecker", moduleVersionChecker)
                    .put("WelcomeMessage", moduleWelcomeMessage).build();

            section = oldConfig.getSection("Settings.PermissionsManager");
            final ImmutableMap<String, Object> permissionsManager = ImmutableMap.<String, Object>builder()
                    .put("defaultPrefix", section.getString("defaultPrefix"))
                    .put("defaultSuffix", section.getString("defaultSuffix")).build();

            section = oldConfig.getSection("Settings.ServerAlias");
            final ImmutableMap<String, String> serverAlias = section.getKeys().stream()
                    .collect(MapUtils.immutableMapCollector(key -> key, section::getString));

            final ImmutableMap<String, Object> configMap = ImmutableMap.<String, Object>builder()
                    .put("AccountDatabase", accountDatabase).put("Formats", formats).put("Modules", modules)
                    .put("PrefixDefaults", permissionsManager).put("ServerAlias", serverAlias).build();

            config = ConfigFactory.parseMap(configMap).withFallback(config.withoutPath("ServerAlias")).resolve();

            // Rename old file
            Files.move(OLD_CONFIG_FILE, OLD_OLD_CONFIG_FILE);

            LoggerHelper.info("Old config file has been renamed to config.old.yml.");

            // Done
            LoggerHelper.info("Migration successful!");
        } catch (Exception e) {
            LoggerHelper.error("There has been an error while migrating the old YAML config file!", e);
        }
    }
}
