package dev.aura.bungeechat.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.util.Logger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
    private static Configuration configuration;

    public static void load() {
        File folder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File cfile = new File(folder, "config.yml");
        try {
            if (!cfile.exists()) {
                Files.copy(ProxyServer.getInstance().getPluginManager().getPlugin("BungeeChat")
                        .getResourceAsStream("config.yml"), cfile.toPath());
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfile);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("There is an error with creating or loading the conifg file!");
            Logger.error("Please contact the author at spigotmc.org!");
        }
    }

    public static void reload() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                    .load(new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/config.yml"));
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

}
