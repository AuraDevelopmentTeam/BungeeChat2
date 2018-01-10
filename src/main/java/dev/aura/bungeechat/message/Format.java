package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Format {
  ALERT("alert"),
  CHAT_LOGGING_CONSOLE("chatLoggingConsole"),
  CHAT_LOGGING_FILE("chatLoggingFile"),
  GLOBAL_CHAT("globalChat"),
  HELP_OP("helpOp"),
  JOIN_MESSAGE("joinMessage"),
  LEAVE_MESSAGE("leaveMessage"),
  LOCAL_CHAT("localChat"),
  LOCAL_SPY("localSpy"),
  MESSAGE_SENDER("messageSender"),
  MESSAGE_TARGET("messageTarget"),
  MOTD("motd"),
  SERVER_SWITCH("serverSwitch"),
  SOCIAL_SPY("socialSpy"),
  STAFF_CHAT("staffChat"),
  WELCOME_MESSAGE("welcomeMessage");

  @Getter private final String stringPath;

  public String get(BungeeChatContext context) {
    return PlaceHolderUtil.getFullFormatMessage(this, context);
  }
}
