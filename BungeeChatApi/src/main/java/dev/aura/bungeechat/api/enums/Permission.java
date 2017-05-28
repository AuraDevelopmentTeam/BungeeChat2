package dev.aura.bungeechat.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    BUNGEECHAT_RELOAD("bungeechat.admin.reload"),

    USE_COLORED_CHAT("bungeechat.chat.colors"),

    BYPASS_ALL("bungeechat.chat.*"),
    BYPASS_ANTI_ADVERTISEMENT("bungeechat.chat.bypassantiadvertisement"),
    BYPASS_ANTI_SPAM("bungeechat.chat.bypassantispam"),
    BYPASS_ANTI_SWEAR("bungeechat.chat.bypassantiswear"),

    COMMAND_ALL("bungeechat.command.*"),
    COMMAND_ALERT("bungeechat.command.alert"),
    COMMAND_GLOBAL("bungeechat.command.global"),
    COMMAND_GLOBAL_TOGGLE("bungeechat.command.global.toggle"),
    COMMAND_STAFFCHAT("bungeechat.command.staffchat"),
    COMMAND_HELPOP("bungeechat.command.helpop"),
    COMMAND_IGNORE("bungeechat.command.ignore"),
    COMMAND_IGNORE_BYPASS("bungeechat.command.ignore.bypass"),
    COMMAND_MESSAGE("bungeechat.command.msg"),
    COMMAND_TOGGLE_MESSAGE("bungeechat.command.toggle"),
    COMMAND_TOGGLE_MESSAGE_BYPASS("bungeechat.command.toggle.bypass"),
    COMMAND_VANISH("bungeechat.command.vanish"),
    COMMAND_VANISH_SEE("bungeechat.command.vanish.see"),
    COMMAND_SOCIALSPY("bungeechat.command.socialspy");

    @Getter
    private final String stringedPermission;
}
