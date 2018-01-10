package dev.aura.bungeechat.message;

import dev.aura.bungeechat.account.BungeecordAccountManager;
import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import java.util.UUID;
import net.md_5.bungee.api.CommandSender;

public class Context extends BungeeChatContext {
  public Context() {
    super();
  }

  public Context(BungeeChatAccount player) {
    super(player);
  }

  public Context(BungeeChatAccount sender, BungeeChatAccount target) {
    super(sender, target);
  }

  public Context(UUID sender) {
    super(AccountManager.getAccount(sender).get());
  }

  public Context(UUID sender, UUID target) {
    super(AccountManager.getAccount(sender).get(), AccountManager.getAccount(target).get());
  }

  public Context(CommandSender sender) {
    super(BungeecordAccountManager.getAccount(sender).get());
  }

  public Context(CommandSender player, String message) {
    this(player);

    setMessage(message);
  }

  public Context(CommandSender sender, CommandSender target) {
    super(
        BungeecordAccountManager.getAccount(sender).get(),
        BungeecordAccountManager.getAccount(target).get());
  }

  public Context(CommandSender sender, CommandSender target, String message) {
    this(sender, target);

    setMessage(message);
  }
}
