package dev.aura.bungeechat.api;

import java.io.File;

import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.BuildType;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;

/**
 * This is the base Interface for the BungeChatApi. The central methods will be
 * found here
 */
public interface BungeeChatApi {
    public static final String ID = "bungeechat";
    public static final String NAME = "Bungee Chat";
    public static final String ID_LIB = ID + "lib";
    public static final String NAME_LIB = NAME + " Lib";
    public static final String DESCRIPTION = "Bungee Chat Plugin";
    public static final String DESCRIPTION_LIB = DESCRIPTION + " Lib";
    public static final String VERSION = "@version@";
    public static final BuildType BUILD_TYPE = BuildType.valueOf("@buildType@");
    public static final int BUILD = Integer.parseInt("@build@");
    public static final String URL = "https://www.spigotmc.org/resources/bungee-chat.12592";
    public static final String AUTHOR_SHAWN = "shawn_ian";
    public static final String AUTHOR_BRAINSTONE = "The_BrainStone";
    public static final String AUTHOR_RYADA = "Ryada";
    public static final String[] AUTHORS = new String[] { AUTHOR_SHAWN, AUTHOR_BRAINSTONE };
    public static final double CONFIG_VERSION = 10.1;
    public static final int PLUGIN_ID = 12592;

    /**
     * Method to retrieve the instance of the API
     *
     * @return The BungeeChatApi instance
     */
    public static BungeeChatApi getInstance() {
        return BungeeChatInstaceHolder.getInstance();
    }

    /**
     * Method the get the server type. In most cases you know what server type
     * you're running on, but in case you need to know, use this.
     *
     * @return One member of the {@link ServerType}
     */
    public ServerType getServerType();

    /**
     * Retrieves (and creates if necessary) the config folder.
     *
     * @return The existing config folder
     */
    public File getConfigFolder();

    /**
     * Method used to check if a user has a certain permission or not.
     *
     * @param account
     *            User to check
     * @param permission
     *            Permission to check.<br>
     *            <small><b>(Note that this argument is of type
     *            {@link Permission}. So only BungeeChat specific permissions
     *            can be checked with this!)</b></small>
     * @return <tt>true</tt> if the user has the permission, <tt>false</tt> if
     *         not
     */
    public boolean hasPermission(BungeeChatAccount account, Permission permission);

    /**
     * Send a private message. The context contains the sender, the target and
     * the message!
     *
     * @param context
     *            Containing sender, target and message.
     * @throws InvalidContextError
     *             Throws and {@link InvalidContextError} if either a sender,
     *             target or message is missing in this context.
     */
    public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError;

    /**
     * Sends a message from the sender in the context to the specified channel.
     * The message has to be in the context.
     *
     * @param context
     *            Containing sender and message.
     * @param channel
     *            What channel to send the message in.
     * @throws InvalidContextError
     *             Throws and {@link InvalidContextError} if either a sender or
     *             message is missing in this context.
     */
    public void sendChannelMessage(BungeeChatContext context, ChannelType channel) throws InvalidContextError;

    /**
     * The same as
     * {@link BungeeChatApi#sendChannelMessage(BungeeChatContext, ChannelType)}.
     * But uses the channel the sender is currently in.
     *
     * @param context
     *            Containing sender and message.
     * @throws InvalidContextError
     *             Throws and {@link InvalidContextError} if either a sender or
     *             message is missing in this context.
     */
    default public void sendChannelMessage(BungeeChatContext context) throws InvalidContextError {
        if (context.hasSender()) {
            sendChannelMessage(context, context.getSender().get().getChannelType());
        } else {
            sendChannelMessage(context, ChannelType.LOCAL);
        }
    }
}
