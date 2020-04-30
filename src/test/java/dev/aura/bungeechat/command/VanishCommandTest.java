package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class VanishCommandTest {
  private static final VanishCommand handler =
      Mockito.mock(VanishCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("on", "off"), tabComplete(""));
    assertEquals(Arrays.asList("on", "off"), tabComplete("o"));
    assertEquals(Arrays.asList("on"), tabComplete("on"));
    assertEquals(Arrays.asList("off"), tabComplete("of"));
    assertEquals(Arrays.asList("off"), tabComplete("off"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("on", ""));
    assertEquals(Arrays.asList(), tabComplete("on", "p"));
    assertEquals(Arrays.asList(), tabComplete("on", "player1"));
    assertEquals(Arrays.asList(), tabComplete("off", ""));
    assertEquals(Arrays.asList(), tabComplete("off", "p"));
    assertEquals(Arrays.asList(), tabComplete("off", "player1"));

    assertEquals(Arrays.asList(), tabComplete("on", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("on", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("on", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("off", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("off", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("off", "xxx", "player1"));
  }
}
