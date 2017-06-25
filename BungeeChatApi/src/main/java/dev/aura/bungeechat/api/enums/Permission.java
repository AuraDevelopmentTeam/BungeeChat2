package dev.aura.bungeechat.api.enums;

import lombok.Getter;

/**
 * Enum which contains all the permissions used by BungeeChat.<br>
 * Used for easy access to all the permission nodes.
 */
public enum Permission {
    BUNGEECHAT_RELOAD("admin.reload"),
    BUNGEECHAT_SETPREFIX("admin.setprefix"),
    BUNGEECHAT_SETSUFFIX("admin.setsuffix"),
    BUNGEECHAT_MODULES("admin.modules"),
    CHECK_VERSION("admin.checkversion"),

    USE_COLORED_CHAT("chat.colors"),

    USE_TAB_COMPLETE("chat.tabcomplete"),

    BYPASS_ANTI_ADVERTISEMENT("chat.bypass.antiadvertisement"),
    BYPASS_ANTI_DUPLICATE("chat.bypass.antiduplicate"),
    BYPASS_ANTI_SWEAR("chat.bypass.antiswear"),
    BYPASS_TOGGLE_MESSAGE("chat.bypass.toggle"),
    BYPASS_CHAT_LOCK("chat.bypass.chatlock"),
    BYPASS_IGNORE("chat.bypass.ignore"),

    COMMAND_ALERT("command.alert"),
    COMMAND_GLOBAL("command.global"),
    COMMAND_GLOBAL_TOGGLE("command.global.toggle"),
    COMMAND_STAFFCHAT("command.staffchat"),
    COMMAND_STAFFCHAT_VIEW("command.staffchat.view"),
    COMMAND_HELPOP("command.helpop"),
    COMMAND_HELPOP_VIEW("command.helpop.view"),
    COMMAND_IGNORE("command.ignore"),
    COMMAND_MESSAGE("command.msg"),
    COMMAND_MUTE("command.mute"),
    COMMAND_TEMPMUTE("command.tempmute"),
    COMMAND_UNMUTE("command.unmute"),
    COMMAND_TOGGLE_MESSAGE("command.toggle"),
    COMMAND_VANISH("command.vanish"),
    COMMAND_VANISH_VIEW("command.vanish.view"),
    COMMAND_SOCIALSPY("command.socialspy"),
    COMMAND_LOCALSPY("command.localspy"),
    COMMAND_CHAT_LOCK("command.chatlock"),
    COMMAND_CLEAR_CHAT("command.clearchat"),

    MESSAGE_JOIN("message.join"),
    MESSAGE_LEAVE("message.leave"),
    MESSAGE_SWITCH("message.switch");

    @Getter
    private final String stringedPermission;
    @Getter
    private final boolean warnOnLackingPermission;

    Permission(String stringedPermission, boolean warnOnLackingPermission) {
        this.stringedPermission = "bungeechat." + stringedPermission;
        this.warnOnLackingPermission = warnOnLackingPermission;
    }

    Permission(String stringedPermission) {
        this(stringedPermission, (stringedPermission.startsWith("command.") || stringedPermission.startsWith("admin."))
                && !stringedPermission.endsWith(".view"));
    }
}
