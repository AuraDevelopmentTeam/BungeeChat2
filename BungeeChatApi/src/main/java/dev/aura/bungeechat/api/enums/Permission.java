package dev.aura.bungeechat.api.enums;

import lombok.Getter;

/**
 * Enum which contains all the permissions used by BungeeChat.<br>
 * Used for easy access to all the permission nodes.
 */
public enum Permission {
    BUNGEECHAT_RELOAD("admin.reload"),

    USE_COLORED_CHAT("chat.colors"),

    BYPASS_ANTI_ADVERTISEMENT("chat.bypass.antiadvertisement"),
    BYPASS_ANTI_SPAM("chat.bypass.antispam"),
    BYPASS_ANTI_SWEAR("chat.bypass.antiswear"),

    COMMAND_ALERT("command.alert"),
    COMMAND_GLOBAL("command.global"),
    COMMAND_GLOBAL_TOGGLE("command.global.toggle"),
    COMMAND_STAFFCHAT("command.staffchat"),
    COMMAND_STAFFCHAT_VIEW("command.staffchat.view"),
    COMMAND_HELPOP("command.helpop"),
    COMMAND_HELPOP_VIEW("command.helpop.view"),
    COMMAND_IGNORE("command.ignore"),
    COMMAND_IGNORE_BYPASS("command.ignore.bypass"),
    COMMAND_MESSAGE("command.msg"),
    COMMAND_TOGGLE_MESSAGE("command.toggle"),
    COMMAND_TOGGLE_MESSAGE_BYPASS("command.toggle.bypass"),
    COMMAND_VANISH("command.vanish"),
    COMMAND_VANISH_SEE("command.vanish.see"),
    COMMAND_SOCIALSPY("command.socialspy"),
    COMMAND_CHAT_LOCK("command.chatlock"),
    COMMAND_CHAT_LOCK_BYPASS("command.chatlock.bypass"),

    MESSAGE_JOIN("message.join"),
    MESSAGE_LEAVE("message.leave");

    @Getter
    private final String stringedPermission;
    @Getter
    private final boolean warnOnLackingPermission;

    private Permission(String stringedPermission, boolean warnOnLackingPermission) {
        this.stringedPermission = "bungeechat." + stringedPermission;
        this.warnOnLackingPermission = warnOnLackingPermission;
    }

    private Permission(String stringedPermission) {
        this(stringedPermission, stringedPermission.startsWith("command."));
    }
}
