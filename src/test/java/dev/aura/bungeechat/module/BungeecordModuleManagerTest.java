package dev.aura.bungeechat.module;

import dev.aura.bungeechat.TestHelper;
import dev.aura.bungeechat.api.module.ModuleManager;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BungeecordModuleManagerTest {
  @BeforeClass
  public static void initBungeeChat() {
    TestHelper.initBungeeChat();
  }

  @AfterClass
  public static void deinitBungeeChat() throws IOException {
    TestHelper.deinitBungeeChat();
  }

  @Test
  public void modulesEnableAndDisableTest() {
    BungeecordModuleManager.registerPluginModules();
    ModuleManager.enableModules();
    ModuleManager.disableModules();
  }
}
