package dev.aura.bungeechat.message;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.AccountType;
import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolder;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.TimeUtil;
import java.text.SimpleDateFormat;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@UtilityClass
public class PlaceHolders {
  private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

  public static void registerPlaceHolders() {
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder("timestamp", context -> TimeUtil.getLongTimeStamp()));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder("time", context -> TimeUtil.getTimeStamp()));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder("short_time", context -> TimeUtil.getShortTimeStamp()));
    PlaceHolderManager.registerPlaceholder(new PlaceHolder("date", context -> TimeUtil.getDate()));
    PlaceHolderManager.registerPlaceholder(new PlaceHolder("day", context -> TimeUtil.getDay()));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder("month", context -> TimeUtil.getMonth()));
    PlaceHolderManager.registerPlaceholder(new PlaceHolder("year", context -> TimeUtil.getYear()));

    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "name",
                context -> context.getSender().get().getName(),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_name"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "displayname",
                context -> context.getSender().get().getDisplayName(),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_displayname"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "fullname",
                context -> HookManager.getFullName(context.getSender().get()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_fullname"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "fulldisplayname",
                context -> HookManager.getFullDisplayName(context.getSender().get()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_displayfullname"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "prefix",
                context -> HookManager.getPrefix(context.getSender().get()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_prefix"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "suffix",
                context -> HookManager.getSuffix(context.getSender().get()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_suffix"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "ping",
                context -> String.valueOf(context.getSender().get().getPing()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_ping"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "uuid",
                context -> context.getSender().get().getUniqueId().toString(),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_uuid"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "servername",
                context -> context.getSender().get().getServerName(),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_servername", "to_servername"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "serveralias",
                context -> ServerAliases.getServerAlias(context.getSender().get().getServerName()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_serveralias", "to_serveralias"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "serverip",
                context -> context.getSender().get().getServerIP(),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_serverip", "to_serverip"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "muted_until",
                context -> getDateFormat().format(context.getSender().get().getMutedUntil()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_muted_until"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "server_online",
                context -> getLocalPlayerCount(context.getSender().get()),
                BungeeChatContext.HAS_SENDER)
            .createAliases("sender_server_online"));

    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_name",
            context -> context.getTarget().get().getName(),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_displayname",
            context -> context.getTarget().get().getDisplayName(),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_fullname",
            context -> HookManager.getFullName(context.getTarget().get()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_fulldisplayname",
            context -> HookManager.getFullDisplayName(context.getTarget().get()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_prefix",
            context -> HookManager.getPrefix(context.getTarget().get()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_suffix",
            context -> HookManager.getSuffix(context.getTarget().get()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_ping",
            context -> String.valueOf(context.getTarget().get().getPing()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_uuid",
            context -> context.getTarget().get().getUniqueId().toString(),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_servername",
            context -> context.getTarget().get().getServerName(),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_serveralias",
            context -> ServerAliases.getServerAlias(context.getTarget().get().getServerName()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_serverip",
            context -> context.getTarget().get().getServerIP(),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_muted_until",
            context -> getDateFormat().format(context.getSender().get().getMutedUntil()),
            BungeeChatContext.HAS_TARGET));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "target_server_online",
            context -> getLocalPlayerCount(context.getTarget().get()),
            BungeeChatContext.HAS_TARGET));

    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
            "channel", context -> context.getChannel().get(), BungeeChatContext.HAS_CHANNEL));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder(
                "message",
                context -> PlaceHolderUtil.escape(context.getMessage().get()),
                BungeeChatContext.HAS_MESSAGE)
            .createAliases("command"));
    PlaceHolderManager.registerPlaceholder(
        new PlaceHolder("network_online", context -> getTotalPlayerCount()));
  }

  private static String getLocalPlayerCount(BungeeChatAccount player) {
    if (player.getAccountType() == AccountType.CONSOLE) return getTotalPlayerCount();

    ProxiedPlayer nativePlayer =
        (ProxiedPlayer) BungeecordAccountManager.getCommandSender(player).get();

    return Integer.toString(nativePlayer.getServer().getInfo().getPlayers().size());
  }

  private static String getTotalPlayerCount() {
    return Integer.toString(ProxyServer.getInstance().getPlayers().size());
  }

  private static SimpleDateFormat getDateFormat() {
    return new SimpleDateFormat(dateFormat);
  }
}
