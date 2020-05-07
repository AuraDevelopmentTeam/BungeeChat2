package dev.aura.bungeechat.config;

import static org.junit.Assert.assertEquals;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.TestHelper;
import dev.aura.bungeechat.api.BungeeChatApi;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationTest {
  @BeforeClass
  public static void initBungeeChat() {
    TestHelper.initBungeeChat();
  }

  @AfterClass
  public static void deinitBungeeChat() throws IOException {
    TestHelper.deinitBungeeChat();
  }

  @Test
  public void versionMatchTest() {
    Config defaultConfig =
        ConfigFactory.parseReader(
            new InputStreamReader(
                BungeeChat.getInstance().getResourceAsStream(Configuration.CONFIG_FILE_NAME),
                StandardCharsets.UTF_8),
            Configuration.PARSE_OPTIONS);

    assertEquals(defaultConfig.getDouble("Version"), BungeeChatApi.CONFIG_VERSION, 0.0);
  }
}
