package dev.aura.bungeechat.api;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
import dev.aura.bungeechat.api.exception.InvalidContextException;
import dev.aura.bungeechat.api.interfaces.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.utils.BungeeChatInstaceHolder;

public interface BungeeChatApi {
    public static final String ID = "bungeechat";
    public static final String NAME = "Bungee Chat";
    public static final String ID_LIB = ID + "lib";
    public static final String NAME_LIB = NAME + " Lib";
    public static final String DESCRIPTION = "Bungee Chat Plugin";
    public static final String DESCRIPTION_LIB = DESCRIPTION + " Lib";
    public static final String VERSION = "@version@";
    public static final int BUILD = Integer.valueOf("@build@");
    public static final String URL = "https://www.spigotmc.org/threads/bungee-chat.93960";
    public static final String AUTHOR_SHAWN = "shawn_ian";
    public static final String AUTHOR_BRAINSTONE = "The_BrainStone";
    public static final String[] AUTHORS = new String[] { AUTHOR_SHAWN, AUTHOR_BRAINSTONE };
    public static final double CONFIG_VERSION = 9.0;
    public static final int PLUGIN_ID = 12592;

    /**
     * Method to retrieve the instance of the API
     * 
     * @return The API instance
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
     * @throws InvalidContextException
     *             Throws and {@link InvalidContextException} if either a
     *             sender, target or message is missing in this context.
     */
    public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextException;

    /**
     * 
     * @param context
     *            Containing sender and message.
     * @param channel
     * @throws InvalidContextException
     *             Throws and {@link InvalidContextException} if either a sender
     *             or message is missing in this context.
     */
    public void sendChannelMessage(BungeeChatContext context, ChannelType channel) throws InvalidContextException;

    /**
     * The same as
     * {@link BungeeChatApi#sendChannelMessage(BungeeChatContext, ChannelType)}.
     * But uses the senders channel.
     * 
     * @param context
     *            Containing sender and message.
     * @throws InvalidContextException
     *             Throws and {@link InvalidContextException} if either a sender
     *             or message is missing in this context.
     */
    default public void sendChannelMessage(BungeeChatContext context) throws InvalidContextException {
        if (context.hasPlayer()) {
            sendChannelMessage(context, context.getPlayer().get().getChannelType());
        } else {
            sendChannelMessage(context, ChannelType.NONE);
        }
    }
}
