package dev.aura.bungeechat.permission;

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
    COMMAND_STAFFCHAT("bungeechat.command.staffchat"),
    COMMAND_HELPOP("bungeechat.command.helpop"),
    COMMAND_IGNORE("bungeechat.command.ignore"),
    COMMAND_MESSAGE("bungeechat.command.msg"),
    COMMAND_TOGGLE_MESSAGE("bungeechat.command.toggle");

    Permission(String stringedPermission){
        this.stringedPermission = stringedPermission;
    }

    private String stringedPermission;

    public String getStringedPermission() { return this.stringedPermission; }

}
