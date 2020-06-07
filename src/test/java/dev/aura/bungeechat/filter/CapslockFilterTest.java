package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import org.junit.Test;

public class CapslockFilterTest {
  private static BungeeChatFilter FILTER = new CapslockFilter(8, 50, true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_CAPSLOCK);

  @Test
  public void complexTest() {
    filterHelper.assertNoException(FILTER, "abcdefg");
    filterHelper.assertNoException(FILTER, "abcdefgh");
    filterHelper.assertNoException(FILTER, "abcdefghijklmnop");
    filterHelper.assertNoException(FILTER, "ABCDEFG");
    filterHelper.assertException(FILTER, "ABCDEFGH");
    filterHelper.assertException(FILTER, "ABCDEFGHIJKLMNOP");

    filterHelper.assertNoException(FILTER, "ABCdefgh");
    filterHelper.assertNoException(FILTER, "ABCDefgh");
    filterHelper.assertException(FILTER, "ABCDEfgh");
    filterHelper.assertNoException(FILTER, "ABCDefghi");
    filterHelper.assertException(FILTER, "ABCDEfghi");
  }

  @Test
  public void consoleTest() {
    final BungeeChatFilter filter = new CapslockFilter(8, 50);

    filterHelper.assertNoException(filter, "test");
  }

  @Test
  public void priorityTest() {
    assertEquals(
        "Returned priority is not as expected.",
        FilterManager.CAPSLOCK_FILTER_PRIORITY,
        FILTER.getPriority());
  }

  @Test
  public void simpleTest() {
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "HELLO WORLD!");
  }
}
