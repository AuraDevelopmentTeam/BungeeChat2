package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class BungeeChatCommandTest extends AccountManagerTest {
  private static final BungeeChatCommand handler =
      Mockito.mock(BungeeChatCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("modules", "reload", "setprefix", "setsuffix"), tabComplete(""));
    assertEquals(Arrays.asList("setprefix", "setsuffix"), tabComplete("s"));
    assertEquals(Arrays.asList("setprefix", "setsuffix"), tabComplete("set"));
    assertEquals(Arrays.asList("setprefix"), tabComplete("setpr"));
    assertEquals(Arrays.asList("setprefix"), tabComplete("setprefix"));
    assertEquals(Arrays.asList("modules"), tabComplete("mod"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(
        Arrays.asList("test", "player1", "player2", "hello"), tabComplete("setprefix", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("setprefix", "p"));
    assertEquals(Arrays.asList("player1"), tabComplete("setprefix", "player1"));
    assertEquals(Arrays.asList("hello"), tabComplete("setprefix", "HeLl"));
    assertEquals(Arrays.asList("test"), tabComplete("setprefix", "tEsT"));
    assertEquals(
        Arrays.asList("test", "player1", "player2", "hello"), tabComplete("setsuffix", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("setsuffix", "p"));
    assertEquals(Arrays.asList("player1"), tabComplete("setsuffix", "player1"));
    assertEquals(Arrays.asList("hello"), tabComplete("setsuffix", "HeLl"));
    assertEquals(Arrays.asList("test"), tabComplete("setsuffix", "tEsT"));

    assertEquals(Arrays.asList(), tabComplete("modules", ""));
    assertEquals(Arrays.asList(), tabComplete("modules", "p"));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1"));
    assertEquals(Arrays.asList(), tabComplete("reload", ""));
    assertEquals(Arrays.asList(), tabComplete("reload", "p"));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "player1"));

    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("setprefix", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("setsuffix", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("modules", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("reload", "player1", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "player1", "xxx", "player1"));
  }
}
