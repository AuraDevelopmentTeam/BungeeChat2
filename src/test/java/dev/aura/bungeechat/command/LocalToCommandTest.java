package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class LocalToCommandTest extends ServerInfoTest {
  private static final LocalToCommand handler =
      Mockito.mock(LocalToCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("main", "hub1", "hub2", "test"), tabComplete(""));
    assertEquals(Arrays.asList("hub1", "hub2"), tabComplete("h"));
    assertEquals(Collections.singletonList("test"), tabComplete("tes"));
    assertEquals(Collections.singletonList("main"), tabComplete("main"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("main", ""));
    assertEquals(Collections.emptyList(), tabComplete("main", "test"));

    assertEquals(Collections.emptyList(), tabComplete("main", "test", ""));
    assertEquals(Collections.emptyList(), tabComplete("main", "test", "test"));
  }
}
