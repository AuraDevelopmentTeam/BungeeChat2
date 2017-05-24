package dev.aura.bungeechat.util;

import net.md_5.bungee.api.ChatColor;

public class Logger {
    public static void error(String text) {
        System.out.println(ChatColor.RED + "Error > " + text);
    }

    public static void info(String text) {
        System.out.println(ChatColor.WHITE + "BungeeChat > " + text);
    }

    public static void normal(String text) {
        System.out.println(text);
    }
}
