package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class ClearChatCommandTest extends ServerInfoTest {
  private static final ClearChatCommand handler =
      Mockito.mock(ClearChatCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("local", "global"), tabComplete(""));
    assertEquals(Collections.singletonList("local"), tabComplete("loc"));
    assertEquals(Collections.singletonList("local"), tabComplete("local"));
    assertEquals(Collections.singletonList("global"), tabComplete("g"));
    assertEquals(Collections.singletonList("global"), tabComplete("global"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(Arrays.asList("main", "hub1", "hub2", "test"), tabComplete("local", ""));
    assertEquals(Arrays.asList("hub1", "hub2"), tabComplete("local", "h"));
    assertEquals(Collections.singletonList("test"), tabComplete("local", "tes"));
    assertEquals(Collections.singletonList("main"), tabComplete("local", "main"));
    assertEquals(Collections.emptyList(), tabComplete("local", "xxx"));

    assertEquals(Collections.emptyList(), tabComplete("global", ""));
    assertEquals(Collections.emptyList(), tabComplete("global", "h"));
    assertEquals(Collections.emptyList(), tabComplete("global", "tes"));
    assertEquals(Collections.emptyList(), tabComplete("global", "main"));
    assertEquals(Collections.emptyList(), tabComplete("global", "xxx"));

    assertEquals(Collections.emptyList(), tabComplete("xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "h"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "tes"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "main"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("local", "main", ""));
    assertEquals(Collections.emptyList(), tabComplete("local", "main", "test"));
    assertEquals(Collections.emptyList(), tabComplete("global", "main", ""));
    assertEquals(Collections.emptyList(), tabComplete("global", "main", "test"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "main", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "main", "test"));

    assertEquals(Collections.emptyList(), tabComplete("local", "main", "test", ""));
    assertEquals(Collections.emptyList(), tabComplete("local", "main", "test", "test"));
    assertEquals(Collections.emptyList(), tabComplete("global", "main", "test", ""));
    assertEquals(Collections.emptyList(), tabComplete("global", "main", "test", "test"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "main", "test", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "main", "test", "test"));
  }
}
