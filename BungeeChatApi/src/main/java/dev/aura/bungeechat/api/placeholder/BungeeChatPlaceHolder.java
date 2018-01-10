package dev.aura.bungeechat.api.placeholder;

public interface BungeeChatPlaceHolder {
  public boolean isContextApplicable(BungeeChatContext context);

  public String apply(String message, BungeeChatContext context);

  public String getName();
}
