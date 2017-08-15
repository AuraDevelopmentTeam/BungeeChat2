package dev.aura.bungeechat.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.util.LoggerHelper;
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
    protected static final File OLD_CONFIG_FILE = new File(BungeeChat.getInstance().getConfigFolder(),
            OLD_CONFIG_FILE_NAME);
    protected static final File OLD_OLD_CONFIG_FILE = new File(BungeeChat.getInstance().getConfigFolder(),
            OLD_OLD_CONFIG_FILE_NAME);
    protected static final ConfigParseOptions PARSE_OPTIONS = ConfigParseOptions.defaults().setAllowMissing(false)
            .setSyntax(ConfigSyntax.CONF);
    protected static final ConfigRenderOptions RENDER_OPTIONS = ConfigRenderOptions.defaults().setOriginComments(false);
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
        Config defaultConfig = ConfigFactory.parseReader(new InputStreamReader(
                BungeeChat.getInstance().getResourceAsStream(CONFIG_FILE_NAME), StandardCharsets.UTF_8), PARSE_OPTIONS);

        if (CONFIG_FILE.exists()) {
            try {
                Config fileConfig = ConfigFactory.parseFile(CONFIG_FILE, PARSE_OPTIONS);

                config = fileConfig.withFallback(defaultConfig);
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

    protected void convertOldConfig() {
        if (OLD_CONFIG_FILE.exists()) {
            convertYAMLConfig();

            OLD_CONFIG_FILE.renameTo(OLD_OLD_CONFIG_FILE);
        }

        switch (String.valueOf(BungeeChatApi.CONFIG_VERSION)) {
        case "11.0":
        default:
            // Up to date. No action needed
        }
    }

    private void convertYAMLConfig() {
        try {
            net.md_5.bungee.config.Configuration oldConfig = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new InputStreamReader(new FileInputStream(OLD_CONFIG_FILE), StandardCharsets.UTF_8));

            @Cleanup
            StringWriter writer = new StringWriter();
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(oldConfig, writer);

            LoggerHelper.info(writer.toString());

            // TODO: convert old config
        } catch (Exception e) {
            LoggerHelper.error("There is an error with creating or loading the old config file!", e);
        }
    }

    protected void saveConfig() {
        try {
            @Cleanup
            PrintWriter writer = new PrintWriter(CONFIG_FILE, StandardCharsets.UTF_8.name());
            String renderedConfig = config.root().render(RENDER_OPTIONS);
            renderedConfig = getHeader() + renderedConfig;

            writer.print(renderedConfig);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            LoggerHelper.error("Something very unexpected happend! Please report this!", e);
        }
    }
}
