package dev.aura.bungeechat.api.placeholder;

public interface BungeeChatPlaceHolder {
  public boolean isContextApplicable(BungeeChatContext context);

  public default boolean matchesName(String name) {
    return getName().equals(name);
  }

  public String getReplacement(String name, BungeeChatContext context);

  public String getName();
}
