package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.placeholder.Context;
import dev.aura.bungeechat.placeholder.PlaceHolderUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

@RequiredArgsConstructor
public enum Message {
    NOT_A_PLAYER("not-player"),
    MUTED("muted"),
    UNMUTE_NOT_MUTED("unmute-not-muted"),
    MUTE_IS_MUTED("mute-is-muted"),
    NO_REPLY("no-reply"),
    REPLY_OFFLINE("reply-offline"),
    PLAYER_NOT_FOUND("player-not-found"),
    ENABLE_GLOBAL("enable-global"),
    ENABLE_LOCAL("enable-local"),
    MESSAGE_YOURSELF("message-yourself"),
    ENABLE_SOCIALSPY("enable-socialspy"),
    DISABLE_SOCIALSPY("disable-socialspy"),
    ENABLE_STAFFCHAT("enable-staffchat"),
    ENABLE_MESSAGER("enable-messager"),
    DISABLE_MESSAGER("disable-messager"),
    ENABLE_VANISH("enable-vanish"),
    DISABLE_VANISH("disable-vanish"),
    ENABLE_CHATLOCK("enable-chatlock"),
    DISABLE_CHATLOCK("disable-chatlock"),
    CHAT_IS_DISABLED("chat-is-locked"),
    GLOBAL_IS_DEFAULT("global-is-default"),
    ANTI_ADVERTISE("anti-advertise"),
    TEMPMUTE("tempmute"),
    ADD_IGNORE("add-ignore"),
    REMOVE_IGNORE("remove-ignore"),
    ALREADY_IGNORED("already-ignored"),
    IGNORE_YOURSELF("ignore-yourself"),
    UNIGNORE_YOURSELF("unignore-yourself"),
    NOT_IGNORED("not-ignored"),
    IGNORE_LIST("ignore-list"),
    ANTI_DUPLICATION("anti-duplication"),
    NO_PERMISSION("no-permission"),
    HAS_INGORED("has-ignored"),
    INCORRECT_USAGE("incorrect-usage"),
    UNMUTE("unmute"),
    MUTE("mute"),
    HAS_MESSAGER_DISABLED("has-messager-disabled"),
    PREFIX_REMOVED("prefix-removed"),
    PREFIX_SET("prefix-set"),
    SUFFIX_REMOVED("suffix-removed"),
    SUFFIX_SET("suffix-set"),
    BACK_TO_LOCAL("back-to-local"),
    NOT_IN_GLOBAL_SERVER("not-in-global-server");

    @Getter
    private final String stringPath;

    public String get() {
        return PlaceHolderUtil.getFullMessage(this);
    }

    public String get(BungeeChatAccount sender) {
        return get(new BungeeChatContext(sender));
    }

    public String get(BungeeChatAccount sender, String command) {
        return get(new BungeeChatContext(sender, command));
    }

    public String get(CommandSender sender) {
        return get(new Context(sender));
    }

    public String get(CommandSender sender, String command) {
        return get(new Context(sender, command));
    }

    public String get(BungeeChatContext context) {
        return PlaceHolderUtil.getFullMessage(this, context);
    }
}
