package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

public class DuplicationFilterTest {
  private static DuplicationFilter FILTER = new DuplicationFilter(2, 1, true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_DUPLICATION);

  @Before
  public void clearFilter() {
    FILTER.clear();
  }

  @Test
  public void complexTest() throws InterruptedException {
    filterHelper.assertNoException(FILTER, "test1");
    filterHelper.assertNoException(FILTER, "test2");
    filterHelper.assertException(FILTER, "test1");
    filterHelper.assertException(FILTER, "test2");
    filterHelper.assertNoException(FILTER, "test3");
    filterHelper.assertException(FILTER, "test2");
    filterHelper.assertNoException(FILTER, "test1");
    filterHelper.assertNoException(FILTER, "test2");

    filterHelper.assertException(FILTER, "test1");
    filterHelper.assertException(FILTER, "test2");

    TimeUnit.SECONDS.sleep(1);

    filterHelper.assertNoException(FILTER, "test1");
    filterHelper.assertNoException(FILTER, "test2");
    filterHelper.assertException(FILTER, "test1");
    filterHelper.assertException(FILTER, "test2");
  }

  @Test
  public void consoleTest() {
    final BungeeChatFilter filter = new DuplicationFilter(0, 0);

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
