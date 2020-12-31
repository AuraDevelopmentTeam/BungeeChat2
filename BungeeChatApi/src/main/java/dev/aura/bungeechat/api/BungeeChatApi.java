package dev.aura.bungeechat.api;

import dev.aura.bungeechat.api.enums.BuildType;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.utils.BungeeChatInstanceHolder;
import dev.aura.lib.version.Version;
import java.io.File;

/** This is the base Interface for the BungeeChatApi. The central methods will be found here */
public interface BungeeChatApi {
  public static final String ID = "bungeechat";
  public static final String NAME = "Bungee Chat";
  public static final String DESCRIPTION = "Bungee Chat Plugin";
  public static final String VERSION_STR = "@version@";
  public static final Version VERSION = new Version(VERSION_STR);
  public static final BuildType BUILD_TYPE = BuildType.valueOf("@buildType@");
  public static final int BUILD = Integer.parseInt("@build@");
  public static final String URL = "https://www.spigotmc.org/resources/bungee-chat.12592";
  public static final String AUTHOR_BRAINSTONE = "BrainStone";
  public static final String AUTHOR_SHAWN = "shawn_ian";
  public static final String[] AUTHORS = new String[] {AUTHOR_BRAINSTONE, AUTHOR_SHAWN};
  public static final String[] CONTRIBUTORS =
      new String[] {
        "AwesomestGamer",
        "Brianetta",
        "CryLegend",
        "gb2233",
        "Hodel1",
        "Luck",
        "MineTech13",
        "n0dai"
      };
  public static final String[] TRANSLATORS =
      new String[] {
        "47429921",
        "Cantibra",
        "DardBrinza",
        "Fantasenf",
        "fjeddy",
        "Garixer",
        "gb2233",
        "Itaquito",
        "marzenie",
        "Maxime_74",
        "Misterio77",
        "povsister",
        "Totten98"
      };
  public static final String[] DONATORS = new String[] {"Breantique", "NickT"};
  public static final double CONFIG_VERSION = 11.7;
  public static final int PLUGIN_ID = 12592;

  /**
   * Method to retrieve the instance of the API
   *
   * @return The BungeeChatApi instance
   */
  public static BungeeChatApi getInstance() {
    return BungeeChatInstanceHolder.getInstance();
  }

  /**
   * Retrieves (and creates if necessary) the config folder.
   *
   * @return The existing config folder
   */
  public File getConfigFolder();

  /**
   * Send a private message. The context contains the sender, the target and the message!
   *
   * @param context Containing sender, target and message.
   * @throws InvalidContextError Throws and {@link InvalidContextError} if either a sender, target
   *     or message is missing in this context.
   */
  public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError;

  /**
   * Sends a message from the sender in the context to the specified channel. The message has to be
   * in the context.
   *
   * @param context Containing sender and message.
   * @param channel What channel to send the message in.
   * @throws InvalidContextError Throws and {@link InvalidContextError} if either a sender or
   *     message is missing in this context.
   */
  public void sendChannelMessage(BungeeChatContext context, ChannelType channel)
      throws InvalidContextError;

  /**
   * The same as {@link BungeeChatApi#sendChannelMessage(BungeeChatContext, ChannelType)}. But uses
   * the channel the sender is currently in.
   *
   * @param context Containing sender and message.
   * @throws InvalidContextError Throws and {@link InvalidContextError} if either a sender or
   *     message is missing in this context.
   */
  public default void sendChannelMessage(BungeeChatContext context) throws InvalidContextError {
    if (context.hasSender()) {
      sendChannelMessage(context, context.getSender().get().getChannelType());
    } else {
      sendChannelMessage(context, ChannelType.LOCAL);
    }
  }
}
