package dev.aura.bungeechat.placeholder;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolder;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.TimeUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaceHolders {
    public static void registerPlaceholders() {
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%timestamp%", context -> TimeUtils.getLongTimeStamp()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%time%", context -> TimeUtils.getTimeStamp()));
        PlaceHolderManager
                .registerPlaceholder(new PlaceHolder("%short_time%", context -> TimeUtils.getShortTimeStamp()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%date%", context -> TimeUtils.getDate()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%day%", context -> TimeUtils.getDay()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%month%", context -> TimeUtils.getMonth()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%year%", context -> TimeUtils.getYear()));

        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%name%", context -> context.getSender().get().getName(),
                BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%displayname%",
                context -> context.getSender().get().getDisplayName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%ping%",
                context -> String.valueOf(context.getSender().get().getPing()), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%uuid%",
                context -> context.getSender().get().getUniqueId().toString(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%servername%",
                context -> context.getSender().get().getServerName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%serverip%",
                context -> context.getSender().get().getServerIP(), BungeeChatContext.HAS_SENDER));

        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_name%",
                context -> context.getSender().get().getName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_displayname%",
                context -> context.getSender().get().getDisplayName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_ping%",
                context -> String.valueOf(context.getSender().get().getPing()), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_uuid%",
                context -> context.getSender().get().getUniqueId().toString(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_servername%",
                context -> context.getSender().get().getServerName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%sender_serverip%",
                context -> context.getSender().get().getServerIP(), BungeeChatContext.HAS_SENDER));

        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_name%",
                context -> context.getTarget().get().getName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_displayname%",
                context -> context.getTarget().get().getDisplayName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_ping%",
                context -> String.valueOf(context.getTarget().get().getPing()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_uuid%",
                context -> context.getTarget().get().getUniqueId().toString(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_servername%",
                context -> context.getTarget().get().getServerName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_serverip%",
                context -> context.getTarget().get().getServerIP(), BungeeChatContext.HAS_TARGET));

        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%channel%", context -> context.getChannel().get(), BungeeChatContext.HAS_CHANNEL));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%message%",
                context -> context.getMessage().get().replace("&", "&&"), BungeeChatContext.HAS_MESSAGE));
    }
}
