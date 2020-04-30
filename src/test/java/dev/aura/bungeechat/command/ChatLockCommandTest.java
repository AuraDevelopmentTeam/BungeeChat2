package dev.aura.bungeechat.command;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.mockito.Mockito;

public class ChatLockCommandTest extends ServerInfoTest {
  private static final ChatLockCommand handler =
      Mockito.mock(ChatLockCommand.class, Mockito.CALLS_REAL_METHODS);

  private static Collection<String> tabComplete(String... args) {
    return handler.tabComplete(null, args);
  }

  @Test
  public void tabCompletefirstArgumentTest() {
    assertEquals(Arrays.asList("local", "global"), tabComplete(""));
    assertEquals(Arrays.asList("local"), tabComplete("loc"));
    assertEquals(Arrays.asList("local"), tabComplete("local"));
    assertEquals(Arrays.asList("global"), tabComplete("g"));
    assertEquals(Arrays.asList("global"), tabComplete("global"));
    assertEquals(Arrays.asList(), tabComplete("xxx"));
  }

  @Test
  public void tabCompleteSecondArgumentTest() {
    assertEquals(Arrays.asList("clear", "main", "hub1", "hub2", "test"), tabComplete("local", ""));
    assertEquals(Arrays.asList("hub1", "hub2"), tabComplete("local", "h"));
    assertEquals(Arrays.asList("test"), tabComplete("local", "tes"));
    assertEquals(Arrays.asList("main"), tabComplete("local", "main"));
    assertEquals(Arrays.asList("clear"), tabComplete("local", "cl"));
    assertEquals(Arrays.asList("clear"), tabComplete("local", "clear"));

    assertEquals(Arrays.asList("clear"), tabComplete("global", ""));
    assertEquals(Arrays.asList(), tabComplete("global", "h"));
    assertEquals(Arrays.asList(), tabComplete("global", "tes"));
    assertEquals(Arrays.asList(), tabComplete("global", "main"));
    assertEquals(Arrays.asList("clear"), tabComplete("global", "cl"));
    assertEquals(Arrays.asList("clear"), tabComplete("global", "clear"));

    assertEquals(Arrays.asList(), tabComplete("xxx", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "h"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "tes"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "main"));
  }

  @Test
  public void tabCompleteThirdArgumentTest() {
    assertEquals(Arrays.asList("clear"), tabComplete("local", "test", ""));
    assertEquals(Arrays.asList("clear"), tabComplete("local", "test", "c"));
    assertEquals(Arrays.asList("clear"), tabComplete("local", "test", "clear"));
    assertEquals(Arrays.asList(), tabComplete("local", "clear", ""));
    assertEquals(Arrays.asList(), tabComplete("local", "test", "test"));

    assertEquals(Arrays.asList(), tabComplete("global", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("global", "test", "c"));
    assertEquals(Arrays.asList(), tabComplete("global", "test", "clear"));
    assertEquals(Arrays.asList(), tabComplete("global", "test", "test"));

    assertEquals(Arrays.asList(), tabComplete("xxx", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "test", "h"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "test", "tes"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "test", "main"));
  }

  @Test
  public void tabCompleteExtraArgumentsTest() {
    assertEquals(Arrays.asList(), tabComplete("local", "main", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("local", "main", "test", "test"));
    assertEquals(Arrays.asList(), tabComplete("global", "main", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("global", "main", "test", "test"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "main", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "main", "test", "test"));

    assertEquals(Arrays.asList(), tabComplete("local", "main", "test", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("local", "main", "test", "test", "test"));
    assertEquals(Arrays.asList(), tabComplete("global", "main", "test", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("global", "main", "test", "test", "test"));
    assertEquals(Arrays.asList(), tabComplete("xxx", "main", "test", "test", ""));
    assertEquals(Arrays.asList(), tabComplete("xxx", "main", "test", "test", "test"));
  }
}
