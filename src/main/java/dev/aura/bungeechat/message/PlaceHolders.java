package dev.aura.bungeechat.message;

import java.text.SimpleDateFormat;

import dev.aura.bungeechat.api.hook.HookManager;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolder;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.api.utils.TimeUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaceHolders {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
                new PlaceHolder("%fullname%", context -> HookManager.getFullname(context.getSender().get()),
                        BungeeChatContext.HAS_SENDER).createAliases("%sender_fullname%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%prefix%", context -> HookManager.getPrefix(context.getSender().get()),
                        BungeeChatContext.HAS_SENDER).createAliases("sender_prefix%"));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%suffix%", context -> HookManager.getSuffix(context.getSender().get()),
                        BungeeChatContext.HAS_SENDER).createAliases("sender_suffix%"));
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
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%muted_until%",
                context -> dateFormat.format(context.getSender().get().getMutedUntil()), BungeeChatContext.HAS_SENDER)
                        .createAliases("%sender_muted_until%"));

        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_name%",
                context -> context.getTarget().get().getName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_displayname%",
                context -> context.getTarget().get().getDisplayName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_fullname%",
                context -> HookManager.getFullname(context.getTarget().get()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_prefix%",
                context -> HookManager.getPrefix(context.getTarget().get()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_suffix%",
                context -> HookManager.getSuffix(context.getTarget().get()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_ping%",
                context -> String.valueOf(context.getTarget().get().getPing()), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_uuid%",
                context -> context.getTarget().get().getUniqueId().toString(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_servername%",
                context -> context.getTarget().get().getServerName(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_serverip%",
                context -> context.getTarget().get().getServerIP(), BungeeChatContext.HAS_TARGET));
        PlaceHolderManager.registerPlaceholder(new PlaceHolder("%target_muted_until%",
                context -> dateFormat.format(context.getSender().get().getMutedUntil()), BungeeChatContext.HAS_TARGET));

        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%channel%", context -> context.getChannel().get(), BungeeChatContext.HAS_CHANNEL));
        PlaceHolderManager.registerPlaceholder(
                new PlaceHolder("%message%", context -> PlaceHolderUtil.escapeAltColorCodes(context.getMessage().get()),
                        BungeeChatContext.HAS_MESSAGE).createAliases("%command%"));
    }
}
