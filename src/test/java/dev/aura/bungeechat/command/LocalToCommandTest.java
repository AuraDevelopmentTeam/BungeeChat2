package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class LocalToCommandTest extends ServerInfoTest {
  private static final LocalToCommand handler =
      Mockito.mock(LocalToCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("main", "hub1", "hub2", "test"), tabComplete(""));
    assertEquals(Arrays.asList("hub1", "hub2"), tabComplete("h"));
    assertEquals(Arrays.asList("test"), tabComplete("tes"));
    assertEquals(Arrays.asList("main"), tabComplete("main"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("main", ""));
    assertEquals(Arrays.asList(), tabComplete("main", "test"));

    assertEquals(Arrays.asList(), tabComplete("main", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("main", "test", "test"));
  }
}
