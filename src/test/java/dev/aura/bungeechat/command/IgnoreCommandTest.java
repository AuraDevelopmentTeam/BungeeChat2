package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class IgnoreCommandTest extends AccountManagerTest {
  private static final IgnoreCommand handler =
      Mockito.mock(IgnoreCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(console, args);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("list", "add", "remove"), tabComplete(""));
    assertEquals(Arrays.asList("add"), tabComplete("a"));
    assertEquals(Arrays.asList("remove"), tabComplete("rem"));
    assertEquals(Arrays.asList("remove"), tabComplete("remove"));
    assertEquals(Arrays.asList("list"), tabComplete("lis"));
    assertEquals(Arrays.asList("list"), tabComplete("list"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("add", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("add", "p"));
    assertEquals(Arrays.asList("player1"), tabComplete("add", "player1"));
    assertEquals(Arrays.asList("hello"), tabComplete("add", "HeLl"));
    assertEquals(Arrays.asList("test"), tabComplete("add", "tEsT"));
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("remove", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("remove", "p"));
    assertEquals(Arrays.asList("player1"), tabComplete("remove", "player1"));
    assertEquals(Arrays.asList("hello"), tabComplete("remove", "HeLl"));
    assertEquals(Arrays.asList("test"), tabComplete("remove", "tEsT"));

    assertEquals(Arrays.asList(), tabComplete("list", ""));
    assertEquals(Arrays.asList(), tabComplete("list", "p"));
    assertEquals(Arrays.asList(), tabComplete("list", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("add", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("add", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("add", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "player1"));

    assertEquals(Arrays.asList(), tabComplete("add", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("add", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("add", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("remove", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("list", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", "player1"));
  }
}
