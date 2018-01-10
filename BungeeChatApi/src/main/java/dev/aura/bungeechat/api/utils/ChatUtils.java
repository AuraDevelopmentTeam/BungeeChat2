package dev.aura.bungeechat.api.utils;

import lombok.experimental.UtilityClass;

/** Simple util class to check wither a entered message is a command or not. */
@UtilityClass
public class ChatUtils {
  /**
   * method to check if a message is a command. Which means it starts with a "/".
   *
   * @param message The message to check
   * @return Whether the passed message is a command or not.
   */
  public static boolean isCommand(String message) {
    return message.startsWith("/");
  }
}
