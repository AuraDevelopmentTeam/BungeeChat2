package dev.aura.bungeechat.api.utils;

import dev.aura.bungeechat.api.BungeeChatApi;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BungeeChatInstaceHolder {
    @Getter
    @Setter
    private static BungeeChatApi instance;
}
