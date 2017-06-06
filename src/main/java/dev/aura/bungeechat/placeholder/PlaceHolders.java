package dev.aura.bungeechat.placeholder;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolder;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.TimeUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaceHolders {
    public static void registerPlaceholders() {
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%timestamp%", context -> TimeUtil.getLongTimeStamp()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%time%", context -> TimeUtil.getTimeStamp()));
        PlaceHolderManager
                .registerPlaceholder(new PlaceHolder("%short_time%", context -> TimeUtil.getShortTimeStamp()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%date%", context -> TimeUtil.getDate()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%day%", context -> TimeUtil.getDay()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%month%", context -> TimeUtil.getMonth()));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%year%", context -> TimeUtil.getYear()));

        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%name%", context -> context.getSender().get().getName(), BungeeChatContext.HAS_SENDER)
                        .createAliases("%sender_name%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%displayname%", context -> context.getSender().get().getDisplayName(),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_displayname%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%ping%", context -> String.valueOf(context.getSender().get().getPing()),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_ping%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%uuid%", context -> context.getSender().get().getUniqueId().toString(),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_uuid%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%servername%", context -> context.getSender().get().getServerName(),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_servername%"));
        PlaceHolderManager
                .registerPlaceholder(new PlaceHolder("%serverip%", context -> context.getSender().get().getServerIP(),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_serverip%"));

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
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%message%", context -> PlaceHolderUtil.escapeAltColorCodes(context.getMessage().get()),
                        BungeeChatContext.HAS_MESSAGE).createAliases("%command%"));
    }
}
