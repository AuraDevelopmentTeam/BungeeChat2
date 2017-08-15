package dev.aura.bungeechat.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.BungeeChat;

public class BungeeChatTest {
    private static BungeeChat bungeeChat;

    @BeforeClass
    public static void initBungeeChat() {
        bungeeChat = new BungeeChat();
    }

    @Test
    public void cacheTest() {
        String expected = bungeeChat.getLatestVersion(true);

        assertSame("Caching should return same object", expected, bungeeChat.getLatestVersion());
        assertNotSame("No caching should not return same object", expected, bungeeChat.getLatestVersion(true));
    }

    @Test
    public void isLatestTest() {
        assertTrue("Version should not be \"error\"be latest", bungeeChat.isLatestVersion());
    }
    
    @Test
    public void queryTest() {
        String result = bungeeChat.getLatestVersion(true);

        assertFalse("Version should not be \"error\"", "error".equals(result));
    }
}
