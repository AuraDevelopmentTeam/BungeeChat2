package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import org.junit.Test;

public class DuplicationFilterTest {
  private static BungeeChatFilter FILTER = new DuplicationFilter(2, true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_DUPLICATION);

  @Test
  public void complexTest() {
    filterHelper.assertNoException(FILTER, "test1");
    filterHelper.assertNoException(FILTER, "test2");
    filterHelper.assertException(FILTER, "test1");
    filterHelper.assertException(FILTER, "test2");
    filterHelper.assertNoException(FILTER, "test3");
    filterHelper.assertException(FILTER, "test2");
    filterHelper.assertNoException(FILTER, "test1");
    filterHelper.assertNoException(FILTER, "test2");
  }

  @Test
  public void consoleTest() throws BlockMessageException {
    final BungeeChatFilter filter = new DuplicationFilter(0);

    filterHelper.assertNoException(filter, "test");
  }

  @Test
  public void priorityTest() {
    assertEquals(
        "Returned priority is not as expected.",
        FilterManager.DUPLICATION_FILTER_PRIORITY,
        FILTER.getPriority());
  }

  @Test
  public void simpleTest() {
    filterHelper.assertNoException(FILTER, "test");
    filterHelper.assertException(FILTER, "test");
  }
}
