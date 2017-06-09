package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Format {
    ALERT("alert"),
    SOCIAL_SPY("socialspy"),
    LOCAL_SPY("localspy"),
    GLOBAL_CHAT("global-chat"),
    MESSAGE_SENDER("message-sender"),
    MESSAGE_TARGET("message-target"),
    STAFF_CHAT("staff-chat"),
    LOCAL_CHAT("local-chat"),
    JOIN_MESSAGE("joinmessage"),
    LEAVE_MESSAGE("leavemessage"),
    HELP_OP("helpop"),
    SERVER_SWITCH("server-switch"),
    CHAT_LOGGING_CONSOLE("chat-logging-console"),
    CHAT_LOGGING_FILE("chat-logging-file");

    @Getter
    private final String stringPath;

    public String get(BungeeChatContext context) {
        return PlaceHolderUtil.getFullFormatMessage(this, context);
    }
}
