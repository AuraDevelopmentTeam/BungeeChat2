package dev.aura.bungeechat.listener;

import com.typesafe.config.Config;
import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.utils.ChatUtils;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.module.MutingModule;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MutingListener implements Listener {
  private final Predicate<String> blockedCommandsPredicate;

  public MutingListener(MutingModule mutingModule) {
    final List<List<String>> commands = new LinkedList<>();

    final Config messengerModuleSection =
        BungeecordModuleManager.MESSENGER_MODULE.getModuleSection();
    final Config globalModuleSection =
        BungeecordModuleManager.GLOBAL_CHAT_MODULE.getModuleSection();
    final Config localModuleSection = BungeecordModuleManager.LOCAL_CHAT_MODULE.getModuleSection();

    if (messengerModuleSection.getBoolean("enabled")
        && !mutingModule.getModuleSection().getBoolean("allowMsg")) {
      // /message
      commands.add(Collections.singletonList("/message"));
      commands.add(messengerModuleSection.getStringList("aliases.message"));
      // /reply
      commands.add(Collections.singletonList("/reply"));
      commands.add(messengerModuleSection.getStringList("aliases.reply"));
    }

    if (globalModuleSection.getBoolean("enabled")) {
      // /global
      commands.add(Collections.singletonList("/global"));
      commands.add(globalModuleSection.getStringList("aliases"));
    }

    if (localModuleSection.getBoolean("enabled")) {
      // /local
      commands.add(Collections.singletonList("/local"));
      commands.add(localModuleSection.getStringList("aliases"));
    }

    commands.add(mutingModule.getModuleSection().getStringList("blockedCommands"));

    blockedCommandsPredicate =
        commands.stream()
            .flatMap(Collection::stream)
            .map(cmd -> (cmd.charAt(0) == '/') ? cmd : ('/' + cmd))
            .distinct()
            .map(Pattern::quote)
            .map(pattern -> pattern + "\\b")
            .map(Pattern::compile)
            .map(Pattern::asPredicate)
            .reduce(Predicate::or)
            .orElse(x -> false);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer sender = (ProxiedPlayer) e.getSender();
    BungeeChatAccount account = BungeecordAccountManager.getAccount(sender).get();

    if (!account.isMuted()) return;

    final String message = e.getMessage();

    if (ChatUtils.isCommand(message) && blockedCommandsPredicate.test(message)) {
      MessagesService.sendMessage(sender, Messages.MUTED.get(account));
      e.setCancelled(true);
    } else {
      final ChannelType channel = account.getChannelType();

      if (channel == ChannelType.STAFF) return;

      e.setCancelled(true);
      MessagesService.sendMessage(sender, Messages.MUTED.get(account));
    }
  }
}
