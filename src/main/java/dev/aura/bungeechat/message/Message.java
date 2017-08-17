package dev.aura.bungeechat.message;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

@RequiredArgsConstructor
public enum Message {
    // Channel Type Messages
    ENABLE_GLOBAL("enableGlobal"),
    ENABLE_STAFFCHAT("enableStaffchat"),
    ENABLE_LOCAL("enableLocal"),
    GLOBAL_IS_DEFAULT("globalIsDefault"),
    BACK_TO_LOCAL("backToLocal"),
    NOT_IN_GLOBAL_SERVER("notInGlobalServer"),

    // Messenger Messages
    MESSAGE_YOURSELF("messageYourself"),
    ENABLE_MESSAGER("enableMessager"),
    DISABLE_MESSAGER("disableMessager"),
    NO_REPLY("noReply"),
    REPLY_OFFLINE("replyOffline"),
    HAS_MESSAGER_DISABLED("hasMessagerDisabled"),

    // Clear Chat
    CLEARED_LOCAL("clearedLocal"),
    CLEARED_GLOBAL("clearedGlobal"),

    // Vanish Messages
    ENABLE_VANISH("enableVanish"),
    DISABLE_VANISH("disableVanish"),

    // Mute Messages
    MUTED("muted"),
    UNMUTE_NOT_MUTED("unmuteNotMuted"),
    MUTE_IS_MUTED("muteIsMuted"),
    UNMUTE("unmute"),
    MUTE("mute"),
    TEMPMUTE("tempmute"),

    // Spy Messages
    ENABLE_SOCIAL_SPY("enableSocialSpy"),
    DISABLE_SOCIAL_SPY("disableSocialSpy"),
    ENABLE_LOCAL_SPY("enableLocalSpy"),
    DISABLE_LOCAL_SPY("disableLocalSpy"),

    // Error Messages
    NOT_A_PLAYER("notPlayer"),
    PLAYER_NOT_FOUND("playerNotFound"),
    INCORRECT_USAGE("incorrectUsage"),
    NO_PERMISSION("noPermission"),

    // Ignore Messages
    HAS_INGORED("hasIgnored"),
    ADD_IGNORE("addIgnore"),
    REMOVE_IGNORE("removeIgnore"),
    ALREADY_IGNORED("alreadyIgnored"),
    IGNORE_YOURSELF("ignoreYourself"),
    UNIGNORE_YOURSELF("unignoreYourself"),
    NOT_IGNORED("notIgnored"),
    IGNORE_LIST("ignoreList"),
    IGNORE_NOBODY("ignoreNobody"),

    // Filter Messages
    ANTI_ADVERTISE("antiAdvertise"),
    ANTI_DUPLICATION("antiDuplication"),

    // ChatLock Messages
    ENABLE_CHATLOCK("enableChatlock"),
    DISABLE_CHATLOCK("disableChatlock"),
    CHAT_IS_DISABLED("chatIsLocked"),

    // Prefix/Suffix Messages
    PREFIX_REMOVED("prefixRemoved"),
    PREFIX_SET("prefixSet"),
    SUFFIX_REMOVED("suffixRemoved"),
    SUFFIX_SET("suffixSet"),

    // Update available Message
    UPDATE_AVAILABLE("updateAvailable");

    private static final Map<String, Message> values = Arrays.stream(values())
            .collect(Collectors.toMap(Message::getStringPath, message -> message));

    public static boolean contains(String message) {
        return values.containsKey(message);
    }

    public static Message getFromStringPath(String message) {
        return values.get(message);
    }

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

    public String get(BungeeChatContext context) {
        return PlaceHolderUtil.getFullMessage(this, context);
    }

    public String get(CommandSender sender) {
        return get(new Context(sender));
    }

    public String get(CommandSender sender, String command) {
        return get(new Context(sender, command));
    }
}
