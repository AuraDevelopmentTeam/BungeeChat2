package dev.aura.bungeechat.api.utils;

import dev.aura.bungeechat.api.BungeeChatApi;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/** This class is used to set the instance returned by {@link BungeeChatApi#getInstance()} */
@UtilityClass
public class BungeeChatInstanceHolder {
  // @formatter:off
  /**
   * The actual BungeeChatApi instance.<br>
   * You shouldn't be using this if you are using the API!
   *
   * <p>-- SETTER -- Sets the BungeeChatApi instance. Don't use it!
   *
   * @param instance Seriously. Don't use this!
   *     <p>-- GETTER -- Returns the BungeeChatApi instance. Don't use this though. Use {@link
   *     BungeeChatApi#getInstance()} instead!
   * @return BungeeChatApi instance
   */
  // @formatter:on
  @Getter @Setter private static BungeeChatApi instance;
}
