package dev.aura.bungeechat.api;

import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.enums.Permission;
import dev.aura.bungeechat.api.enums.ServerType;
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

    public static BungeeChatApi getInstance() {
        return BungeeChatInstaceHolder.getInstance();
    }

    public ServerType getServerType();

    public boolean hasPermission(BungeeChatAccount account, Permission permission);

    public void sendPrivateMessage(BungeeChatContext context);

    public void sendChannelMessage(BungeeChatContext context, ChannelType channel);

    default public void sendChannelMessage(BungeeChatContext context) {
        if (context.hasPlayer()) {
            sendChannelMessage(context, context.getPlayer().get().getChannelType());
        } else {
            sendChannelMessage(context, ChannelType.NONE);
        }
    }
}
