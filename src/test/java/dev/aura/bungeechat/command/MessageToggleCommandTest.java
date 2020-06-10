package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageToggleCommandTest extends AccountManagerTest {
  private static final MessageToggleCommand handler =
      Mockito.mock(MessageToggleCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(console, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete(""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("p"));
    assertEquals(Arrays.asList("player1"), tabComplete("player1"));
    assertEquals(Arrays.asList("hello"), tabComplete("HeLl"));
    assertEquals(Arrays.asList("test"), tabComplete("tEsT"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("player1", ""));
    assertEquals(Arrays.asList(), tabComplete("player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx"));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx", "xxx"));
  }
}
