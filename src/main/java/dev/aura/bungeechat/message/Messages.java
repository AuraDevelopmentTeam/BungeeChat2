package dev.aura.bungeechat.message;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.lib.messagestranslator.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

@RequiredArgsConstructor
public enum Messages implements Message {
  // Channel Type Messages
  ENABLE_GLOBAL("enableGlobal"),
  ENABLE_STAFFCHAT("enableStaffchat"),
  ENABLE_LOCAL("enableLocal"),
  GLOBAL_IS_DEFAULT("globalIsDefault"),
  LOCAL_IS_DEFAULT("localIsDefault"),
  BACK_TO_DEFAULT("backToDefault"),
  NOT_IN_GLOBAL_SERVER("notInGlobalServer"),
  NOT_IN_LOCAL_SERVER("notInLocalServer"),

  // Messenger Messages
  MESSAGE_YOURSELF("messageYourself"),
  ENABLE_MESSENGER("enableMessenger"),
  ENABLE_MESSENGER_OTHERS("enableMessengerOthers"),
  DISABLE_MESSENGER("disableMessenger"),
  DISABLE_MESSENGER_OTHERS("disableMessengerOthers"),
  NO_REPLY("noReply"),
  REPLY_OFFLINE("replyOffline"),
  HAS_MESSENGER_DISABLED("hasMessengerDisabled"),

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
  UNKNOWN_SERVER("unknownServer"),

  // Ignore Messages
  HAS_IGNORED("hasIgnored"),
  ADD_IGNORE("addIgnore"),
  REMOVE_IGNORE("removeIgnore"),
  ALREADY_IGNORED("alreadyIgnored"),
  IGNORE_YOURSELF("ignoreYourself"),
  UNIGNORE_YOURSELF("unignoreYourself"),
  NOT_IGNORED("notIgnored"),
  IGNORE_LIST("ignoreList"),
  IGNORE_NOBODY("ignoreNobody"),
  MESSAGE_BLANK("messageBlank"),

  // Filter Messages
  ANTI_ADVERTISE("antiAdvertise"),
  ANTI_CAPSLOCK("antiCapslock"),
  ANTI_DUPLICATION("antiDuplication"),
  ANTI_SPAM("antiSpam"),

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

  @Getter private final String stringPath;

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
