package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class VanishCommandTest {
  private static final VanishCommand handler =
      Mockito.mock(VanishCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("on", "off"), tabComplete(""));
    assertEquals(Arrays.asList("on", "off"), tabComplete("o"));
    assertEquals(Collections.singletonList("on"), tabComplete("on"));
    assertEquals(Collections.singletonList("off"), tabComplete("of"));
    assertEquals(Collections.singletonList("off"), tabComplete("off"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("on", ""));
    assertEquals(Collections.emptyList(), tabComplete("on", "p"));
    assertEquals(Collections.emptyList(), tabComplete("on", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("off", ""));
    assertEquals(Collections.emptyList(), tabComplete("off", "p"));
    assertEquals(Collections.emptyList(), tabComplete("off", "player1"));

    assertEquals(Collections.emptyList(), tabComplete("on", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("on", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("on", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("off", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("off", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("off", "xxx", "player1"));
  }
}
