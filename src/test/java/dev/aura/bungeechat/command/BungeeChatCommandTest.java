package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class BungeeChatCommandTest extends AccountManagerTest {
  private static final BungeeChatCommand handler =
      Mockito.mock(BungeeChatCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompleteFirstArgumentTest() {
    assertEquals(Arrays.asList("modules", "reload", "setprefix", "setsuffix"), tabComplete(""));
    assertEquals(Arrays.asList("setprefix", "setsuffix"), tabComplete("s"));
    assertEquals(Arrays.asList("setprefix", "setsuffix"), tabComplete("set"));
    assertEquals(Collections.singletonList("setprefix"), tabComplete("setpr"));
    assertEquals(Collections.singletonList("setprefix"), tabComplete("setprefix"));
    assertEquals(Collections.singletonList("modules"), tabComplete("mod"));
    assertEquals(Collections.emptyList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(
        Arrays.asList("test", "player1", "player2", "hello"), tabComplete("setprefix", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("setprefix", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("setprefix", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("setprefix", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("setprefix", "tEsT"));
    assertEquals(
        Arrays.asList("test", "player1", "player2", "hello"), tabComplete("setsuffix", ""));
    assertEquals(Arrays.asList("player1", "player2"), tabComplete("setsuffix", "p"));
    assertEquals(Collections.singletonList("player1"), tabComplete("setsuffix", "player1"));
    assertEquals(Collections.singletonList("hello"), tabComplete("setsuffix", "HeLl"));
    assertEquals(Collections.singletonList("test"), tabComplete("setsuffix", "tEsT"));

    assertEquals(Collections.emptyList(), tabComplete("modules", ""));
    assertEquals(Collections.emptyList(), tabComplete("modules", "p"));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("reload", ""));
    assertEquals(Collections.emptyList(), tabComplete("reload", "p"));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "player1"));

    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("setprefix", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("setsuffix", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("modules", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("reload", "player1", "xxx", "player1"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", ""));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "p"));
    assertEquals(Collections.emptyList(), tabComplete("xxx", "player1", "xxx", "player1"));
  }
}
