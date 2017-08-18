package dev.aura.bungeechat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.config.Configuration;

public class BungeeChatTest {
    private static BungeeChat bungeeChat;

    @BeforeClass
    public static void initBungeeChat() {
        bungeeChat = new BungeeChat();
        BungeeChat.instance = bungeeChat;
        
        Configuration.load();
    }
    
    @AfterClass
    public static void deinitBungeeChat() throws IOException {
        FileUtils.deleteDirectory(bungeeChat.getConfigFolder());
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
