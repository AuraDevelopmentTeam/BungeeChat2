package dev.aura.bungeechat.api.module;

public interface BungeeChatModule {
  public String getName();

  public boolean isEnabled();

  public void onEnable();

  public void onDisable();
}
