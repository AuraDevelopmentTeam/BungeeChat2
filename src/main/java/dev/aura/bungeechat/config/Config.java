package dev.aura.bungeechat.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.util.LoggerHelper;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
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
                Files.copy(ProxyServer.getInstance().getPluginManager().getPlugin("BungeeChat")
                        .getResourceAsStream("config.yml"), cfile.toPath());
            }

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        } catch (Exception e) {
            LoggerHelper.error("There is an error with creating or loading the conifg file!", e);
            LoggerHelper.error("Please contact the author at spigotmc.org!");
        }
    }

    public static void reload() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(getConfigFile());
        } catch (IOException e) {
            e.printStackTrace();
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
}
