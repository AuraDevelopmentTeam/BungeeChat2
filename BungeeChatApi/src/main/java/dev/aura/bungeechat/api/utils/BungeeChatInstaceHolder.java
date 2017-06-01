package dev.aura.bungeechat.api.utils;

import dev.aura.bungeechat.api.BungeeChatApi;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * This class is used to set the instance returned by
 * {@link BungeeChatApi#getInstance()}
 */
@UtilityClass
public class BungeeChatInstaceHolder {
    // @formatter:off
    /**
     * The actual BungeeChatApi instance.<br>
     * You shouldn't be using this if you are using the API!
     *
     * -- SETTER --
     * Sets the BungeeChatApi instance. Don't use it!
     *
     * @param instance
     *            Seriously. Don't use this!
     *
     * -- GETTER --
     * Returns the BungeeChatApi instance. Don't use this though. Use
     * {@link BungeeChatApi#getInstance()} instead!
     *
     * @returns BungeeChatApi instance
     */
    // @formatter:on
    @Getter
    @Setter
    private static BungeeChatApi instance;
}
