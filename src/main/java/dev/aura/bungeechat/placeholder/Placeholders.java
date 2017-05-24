package dev.aura.bungeechat.placeholder;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.placeholder.Placeholder;
import dev.aura.bungeechat.api.utils.TimeUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Placeholders {
    public static void registerPlaceholders() {
        PlaceHolderManager
                .registerPlaceholder(new Placeholder("%data_time%", context -> TimeUtils.getCurrentTimeStamp()));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%data_day%", context -> TimeUtils.getCurrentDay()));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%data_month%", context -> TimeUtils.getCurrentMonth()));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%data_year%", context -> TimeUtils.getCurrentYear()));

        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_name%",
                context -> context.getPlayer().get().getName(), BungeeChatContext.HAS_PLAYER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_displayname%",
                context -> context.getPlayer().get().getDisplayName(), BungeeChatContext.HAS_PLAYER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_ping%",
                context -> String.valueOf(context.getPlayer().get().getPing()), BungeeChatContext.HAS_PLAYER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_uuid%",
                context -> context.getPlayer().get().getUniqueId().toString(), BungeeChatContext.HAS_PLAYER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_servername%",
                context -> context.getPlayer().get().getServerName(), BungeeChatContext.HAS_PLAYER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%player_serverip%",
                context -> context.getPlayer().get().getServerIP(), BungeeChatContext.HAS_PLAYER));

        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_name%",
                context -> context.getSender().get().getName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_displayname%",
                context -> context.getSender().get().getDisplayName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_ping%",
                context -> String.valueOf(context.getSender().get().getPing()), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_uuid%",
                context -> context.getSender().get().getUniqueId().toString(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_servername%",
                context -> context.getSender().get().getServerName(), BungeeChatContext.HAS_SENDER));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%sender_serverip%",
                context -> context.getSender().get().getServerIP(), BungeeChatContext.HAS_SENDER));

        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_name%",
                context -> context.getTarget().get().getName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_displayname%",
                context -> context.getTarget().get().getDisplayName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_ping%",
                context -> String.valueOf(context.getTarget().get().getPing()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_uuid%",
                context -> context.getTarget().get().getUniqueId().toString(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_servername%",
                context -> context.getTarget().get().getServerName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new Placeholder("%target_serverip%",
                context -> context.getTarget().get().getServerIP(), BungeeChatContext.HAS_TARGET));
    }
}
