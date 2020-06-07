package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SpamFilterTest {
  private static SpamFilter FILTER = new SpamFilter(3, true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_SPAM);

  @BeforeClass
  public static void setupFilter() {
    SpamFilter.expiryTimer = TimeUnit.SECONDS.toNanos(5);
  }

  @Before
  public void clearFilter() {
    FILTER.clear();
  }

  @Test
  public void complexTest() throws InterruptedException {
    filterHelper.assertNoException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertNoException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertNoException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(2);

    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");

    TimeUnit.SECONDS.sleep(5);

    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertNoException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
    filterHelper.assertException(FILTER, "hello world!");
  }

  @Test
  public void consoleTest() {
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
