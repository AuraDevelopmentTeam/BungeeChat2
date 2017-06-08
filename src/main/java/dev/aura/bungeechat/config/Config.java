package dev.aura.bungeechat.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    public static void load() {
        File cfile = getConfigFile();

        try {
            if (!cfile.exists()) {
                copyDefaultConfig(cfile);
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);

            if (BungeeChat.CONFIG_VERSION > configuration.getDouble("Version")) {
                File newConfig = getNewConfigFile();

                LoggerHelper.warning("--------------------------------------------------------------------------------");
                LoggerHelper.warning(
                        "\007Your config is outdated and might cause errors when been used with this version of BungeeChat! Please update your config.");
                LoggerHelper.warning("The current default config has been generated in " + newConfig.getAbsolutePath()
                        + ". Simply copy settings into the new config and run \"bungeechat reload\".");
                LoggerHelper.warning("--------------------------------------------------------------------------------");

                copyDefaultConfig(newConfig);

                Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            }
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
        Files.copy(BungeeChat.getInstance().getResourceAsStream("config.yml"), destination.toPath());
    }
}
