package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Format {
    ALERT("alert"),
    SOCIAL_SPY("socialSpy"),
    LOCAL_SPY("localSpy"),
    GLOBAL_CHAT("globalChat"),
    MESSAGE_SENDER("messageSender"),
    MESSAGE_TARGET("messageTarget"),
    STAFF_CHAT("staffChat"),
    LOCAL_CHAT("localChat"),
    JOIN_MESSAGE("joinMessage"),
    LEAVE_MESSAGE("leaveMessage"),
    HELP_OP("helpOp"),
    SERVER_SWITCH("serverSwitch"),
    CHAT_LOGGING_CONSOLE("chatLoggingConsole"),
    CHAT_LOGGING_FILE("chatLoggingFile");

    @Getter
    private final String stringPath;

    public String get(BungeeChatContext context) {
        return PlaceHolderUtil.getFullFormatMessage(this, context);
    }
}
