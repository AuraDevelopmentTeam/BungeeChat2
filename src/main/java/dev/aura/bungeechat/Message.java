package dev.aura.bungeechat;

import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.config.Config;
import dev.aura.bungeechat.placeholder.Context;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum Message {

    NOT_A_PLAYER("Messages.not-player"),
    MUTED("Messages.muted"),
    UNMUTE_NOT_MUTED("Messages.unmute-not-muted"),
    MUTE_IS_MUTED("Messages.mute-is-muted"),
    NO_REPLY("Messages.no-reply"),
    REPLY_OFFLINE("Messages.reply-offline"),
    PLAYER_NOT_FOUND("Messages.player-not-found"),
    ENABLE_GLOBAL("Messages.enable-global"),
    DISABLE_GLOBAL("Messages.disable-global"),
    MESSAGE_YOURSELF("Messages.message-yourself"),
    ENABLE_SOCIALSPY("Messages.enable-socialspy"),
    DISABLE_SOCIALSPY("Messages.disable-socialspy"),
    ENABLE_STAFFCHAT("Messages.enable-staffchat"),
    DISABLE_STAFFCHAT("Messages.disable-staffchat"),
    ENABLE_MESSAGER("Messages.enable-messager"),
    DISABLE_MESSAGER("Messages.disable-messager"),
    ENABLE_VANISH("Messages.enable-vanish"),
    DISABLE_VANISH("Messages.disable-vanish"),
    ENABLE_CHATLOCK("Messages.enable-chatlock"),
    DISABLE_CHATLOCK("Messages.disable-chatlock"),
    CHAT_IS_DISABLED("Messages.chat-is-locked"),
    GLOBAL_IS_DEFAULT("Messages.global-is-default"),
    ANTI_ADVERTISE("Messages.anti-advertise"),
    ALREADY_IGNORED("Messages.already-ignored"),
    IGNORE_YOURSELF("Messages.ignore-yourself"),
    UNIGNORE_YOURSELF("Messages.unignore-yourself"),
    NOT_IGNORED("Messages.not-ignored"),
    ANTI_DUPLICATION("Messages.anti-duplication"),
    INCORRECT_USAGE("Messages.incorrect-usage"),
    UNMUTE("Messages.unmute"),
    MUTE("Messages.mute"),
    TEMPMUTE("Messages.tempmute"),
    IGNORE("Messages.add-ignore"),
    UNIGORE("Messages.remove-ignore"),
    HAS_MESSAGER_DISABLED("Messages.has-messager-disabled");

    private final String stringPath;

    public String get() {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(this.stringPath));
        return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

    public String get(CommandSender sender) {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(this.stringPath));
        if (sender instanceof ProxiedPlayer) return PlaceHolderManager.processMessage(rawMessage, new Context((ProxiedPlayer) sender));
        else return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

    public String get(CommandSender sender, String command) {
        String rawMessage = ChatColor.translateAlternateColorCodes('&', Config.get().getString(this.stringPath));
        rawMessage = rawMessage.replace("%command%", command);
        if (sender instanceof ProxiedPlayer) return PlaceHolderManager.processMessage(rawMessage, new Context((ProxiedPlayer) sender));
        else return PlaceHolderManager.processMessage(rawMessage, new Context());
    }

}

