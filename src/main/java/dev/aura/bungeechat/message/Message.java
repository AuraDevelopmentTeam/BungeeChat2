package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.placeholder.Context;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum Message {
    NOT_A_PLAYER("Mnot-player"),
    MUTED("muted"),
    UNMUTE_NOT_MUTED("unmute-not-muted"),
    MUTE_IS_MUTED("mute-is-muted"),
    NO_REPLY("no-reply"),
    REPLY_OFFLINE("reply-offline"),
    PLAYER_NOT_FOUND("player-not-found"),
    ENABLE_GLOBAL("enable-global"),
    DISABLE_GLOBAL("disable-global"),
    MESSAGE_YOURSELF("message-yourself"),
    ENABLE_SOCIALSPY("enable-socialspy"),
    DISABLE_SOCIALSPY("disable-socialspy"),
    ENABLE_STAFFCHAT("enable-staffchat"),
    DISABLE_STAFFCHAT("disable-staffchat"),
    ENABLE_MESSAGER("enable-messager"),
    DISABLE_MESSAGER("disable-messager"),
    ENABLE_VANISH("enable-vanish"),
    DISABLE_VANISH("disable-vanish"),
    ENABLE_CHATLOCK("enable-chatlock"),
    DISABLE_CHATLOCK("disable-chatlock"),
    CHAT_IS_DISABLED("chat-is-locked"),
    GLOBAL_IS_DEFAULT("global-is-default"),
    ANTI_ADVERTISE("anti-advertise"),
    ALREADY_IGNORED("already-ignored"),
    IGNORE_YOURSELF("ignore-yourself"),
    UNIGNORE_YOURSELF("unignore-yourself"),
    NOT_IGNORED("not-ignored"),
    ANTI_DUPLICATION("anti-duplication"),
    INCORRECT_USAGE("incorrect-usage"),
    UNMUTE("unmute"),
    MUTE("mute"),
    TEMPMUTE("tempmute"),
    IGNORE("add-ignore"),
    UNIGORE("remove-ignore"),
    HAS_MESSAGER_DISABLED("has-messager-disabled"),
    HAS_INGORED("has-ignored");

    private final String stringPath;
    
    private Message (String stringPath) {
        this.stringPath = "Messages." + stringPath;
    }

    public String get() {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(stringPath));
        
        return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

    public String get(CommandSender sender) {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(stringPath));
        
        if (sender instanceof ProxiedPlayer)
            return PlaceHolderManager.processMessage(rawMessage, new Context((ProxiedPlayer) sender));
        else
            return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

    public String get(CommandSender sender, String command) {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(stringPath));
        rawMessage = rawMessage.replace("%command%", command);
        
        if (sender instanceof ProxiedPlayer)
            return PlaceHolderManager.processMessage(rawMessage, new Context((ProxiedPlayer) sender));
        else
            return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

}
