package dev.aura.bungeechat;

import net.alpenblock.bungeeperms.ChatColor;

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
