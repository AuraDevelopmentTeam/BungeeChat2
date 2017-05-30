package dev.aura.bungeechat.api.enums;

import lombok.Getter;

public enum Permission {
    BUNGEECHAT_RELOAD("admin.reload"),

    USE_COLORED_CHAT("chat.colors"),

    BYPASS_ALL("chat.*"),
    BYPASS_ANTI_ADVERTISEMENT("chat.bypassantiadvertisement"),
    BYPASS_ANTI_SPAM("chat.bypassantispam"),
    BYPASS_ANTI_SWEAR("chat.bypassantiswear"),

    COMMAND_ALL("command.*"),
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
    COMMAND_SOCIALSPY("command.socialspy");

    @Getter
    private final String stringedPermission;

    private Permission(String stringedPermission) {
        this.stringedPermission = "bungeechat." + stringedPermission;
    }
}
