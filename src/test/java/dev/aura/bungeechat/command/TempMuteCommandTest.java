package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.AccountManagerTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class TempMuteCommandTest extends AccountManagerTest {
  private static final TempMuteCommand handler =
      Mockito.mock(TempMuteCommand.class, Mockito.CALLS_REAL_METHODS);

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
  public void tabCompleteSecondArgumentTest() {
    assertEquals(
        Arrays.asList(
            "<duration>s",
            "<duration>m",
            "<duration>h",
            "<duration>d",
            "<duration>w",
            "<duration>mo",
            "<duration>y"),
        tabComplete("player1", ""));
    assertEquals(
        Arrays.asList("1s", "1m", "1h", "1d", "1w", "1mo", "1y"), tabComplete("player1", "1"));
    assertEquals(
        Arrays.asList("123s", "123m", "123h", "123d", "123w", "123mo", "123y"),
        tabComplete("player1", "123"));
    assertEquals(
        Arrays.asList("12.s", "12.m", "12.h", "12.d", "12.w", "12.mo", "12.y"),
        tabComplete("player1", "12."));
    assertEquals(
        Arrays.asList("12.34s", "12.34m", "12.34h", "12.34d", "12.34w", "12.34mo", "12.34y"),
        tabComplete("player1", "12.34"));
    assertEquals(Arrays.asList("1m", "1mo"), tabComplete("player1", "1m"));
    assertEquals(Arrays.asList("1mo"), tabComplete("player1", "1mo"));
    assertEquals(Arrays.asList("1y"), tabComplete("player1", "1y"));
    assertEquals(Arrays.asList(), tabComplete("player1", "1x"));
    assertEquals(Arrays.asList(), tabComplete("player1", "1xxx"));
    assertEquals(Arrays.asList(), tabComplete("player1", "s"));
    assertEquals(Arrays.asList(), tabComplete("player1", "xxx"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", ""));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "p"));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "player1"));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "xxx"));

    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "xxx", "p"));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "xxx", "player1"));
    assertEquals(Arrays.asList(), tabComplete("player1", "123d", "xxx", "xxx"));
  }
}
