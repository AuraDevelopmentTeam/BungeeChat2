package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import org.junit.Before;
import org.junit.Test;

public class SpamFilterTest {
  private static SpamFilter FILTER = new SpamFilter(3, true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_SPAM);

  @Before
  public void clearFilter() {
    FILTER.clear();
  }

  @Test
  public void complexTest() {
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");

    // Alternatively I could wait a minute. Let's not
    FILTER.clear();

    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
  }

  @Test
  public void consoleTest() throws BlockMessageException {
    final BungeeChatFilter filter = new SpamFilter(3);

    filterHelper.assertNoException(filter, "test");
  }

  @Test
  public void priorityTest() {
    assertEquals(
        "Returned priority is not as expected.",
        FilterManager.SPAM_FILTER_PRIORITY,
        FILTER.getPriority());
  }

  @Test
  public void simpleTest() {
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
  }
}
