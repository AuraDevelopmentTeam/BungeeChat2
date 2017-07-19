package dev.aura.bungeechat.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.util.LoggerHelper;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

@UtilityClass
public class Config {
    private static Configuration configuration;
    private static boolean firstStart = true;

    public static void load() {
        File cfile = getConfigFile();

        try {
            if (!cfile.exists()) {
                copyDefaultConfig(cfile);
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new InputStreamReader(new FileInputStream(cfile), "UTF-8"));

            if (BungeeChatApi.CONFIG_VERSION > configuration.getDouble("Version")) {
                final File newConfig = getNewConfigFile();
                final String line = "----------------------------------------------------------------------------------------------------------------------------------";

                copyDefaultConfig(newConfig);

                LoggerHelper.warning(line);
                LoggerHelper.warning(
                        "\007Your config is outdated and might cause errors when being used with this version of BungeeChat! Please update your config.");
                LoggerHelper.warning(
                        "The current default config has been generated in " + newConfig.getAbsolutePath() + '.');
                LoggerHelper.warning(
                        "Simply copy your old settings into the new config, rename it to \"config.yml\" and run \"bungeechat reload\".");

                if (firstStart) {
                    LoggerHelper.warning("The server will continue starting after 10 seconds.");
                    LoggerHelper.warning(line);

                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                } else {
                    LoggerHelper.warning(line);
                }
            }

            firstStart = false;
        } catch (Exception e) {
            LoggerHelper.error("There is an error with creating or loading the conifg file!", e);
            LoggerHelper.error("Please contact the authors at http://discord.me/bungeechat!");
        }
    }

    public static Configuration get() {
        return configuration;
    }

    public static double getVersion() {
        return BungeeChatApi.CONFIG_VERSION;
    }

    private static File getConfigFile() {
        return new File(BungeeChat.getInstance().getConfigFolder(), "config.yml");
    }

    private static File getNewConfigFile() {
        return new File(BungeeChat.getInstance().getConfigFolder(), "config.new.yml");
    }

    private static void copyDefaultConfig(File destination) throws IOException {
        Files.copy(BungeeChat.getInstance().getResourceAsStream("config.yml"), destination.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
