package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class IgnoreCommandTest extends AccountManagerTest {
  private static final IgnoreCommand handler =
      Mockito.mock(IgnoreCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(console, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("list", "add", "remove"), tabComplete(""));
    assertEquals(Collections.singletonList("add"), tabComplete("a"));
    assertEquals(Collections.singletonList("remove"), tabComplete("rem"));
    assertEquals(Collections.singletonList("remove"), tabComplete("remove"));
    assertEquals(Collections.singletonList("list"), tabComplete("lis"));
    assertEquals(Collections.singletonList("list"), tabComplete("list"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("add", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("add", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("add", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("add", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("add", "tEsT"));
    assertEquals(Arrays.asList("test", "player1", "player2", "hello"), tabComplete("remove", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("remove", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("remove", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("remove", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("remove", "tEsT"));

    assertEquals(Collections.emptyList(), tabComplete("list", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "player1"));

    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("add", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("remove", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("list", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "player1"));
  }
}
