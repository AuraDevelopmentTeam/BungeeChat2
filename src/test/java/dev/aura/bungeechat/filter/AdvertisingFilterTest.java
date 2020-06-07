package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.message.Messages;
import java.util.Arrays;
import org.junit.Test;

public class AdvertisingFilterTest {
  private static final BungeeChatFilter FILTER =
      new AdvertisingFilter(Arrays.asList("www.google.com", "*.net"), true);
  private static final FilterHelper filterHelper = new FilterHelper(Messages.ANTI_ADVERTISE);

  @Test
  public void consoleTest() {
    final BungeeChatFilter filter = new AdvertisingFilter(Arrays.asList());

    filterHelper.assertNoException(filter, "test");
  }

  @Test
  public void priorityTest() {
    assertEquals(
        "Returned priority is not as expected.",
        FilterManager.ADVERTISING_FILTER_PRIORITY,
        FILTER.getPriority());
  }

  @Test
  public void urlFormatTest() {
    filterHelper.assertException(FILTER, "web.de");
    filterHelper.assertException(FILTER, "http://web.de");
    filterHelper.assertException(FILTER, "https://web.de");
    filterHelper.assertException(FILTER, "www.web.de");
    filterHelper.assertException(FILTER, "http://www.web.de");
    filterHelper.assertException(FILTER, "https://www.web.de");
    filterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text web.de");
    filterHelper.assertException(FILTER, "text http://web.de");
    filterHelper.assertException(FILTER, "text https://web.de");
    filterHelper.assertException(FILTER, "text www.web.de");
    filterHelper.assertException(FILTER, "text http://www.web.de");
    filterHelper.assertException(FILTER, "text https://www.web.de");
    filterHelper.assertException(FILTER, "text web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text http://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text https://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text http://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "text https://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "web.de foobar");
    filterHelper.assertException(FILTER, "http://web.de foobar");
    filterHelper.assertException(FILTER, "https://web.de foobar");
    filterHelper.assertException(FILTER, "www.web.de foobar");
    filterHelper.assertException(FILTER, "http://www.web.de foobar");
    filterHelper.assertException(FILTER, "https://www.web.de foobar");
    filterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "text web.de foobar");
    filterHelper.assertException(FILTER, "text http://web.de foobar");
    filterHelper.assertException(FILTER, "text https://web.de foobar");
    filterHelper.assertException(FILTER, "text www.web.de foobar");
    filterHelper.assertException(FILTER, "text http://www.web.de foobar");
    filterHelper.assertException(FILTER, "text https://www.web.de foobar");
    filterHelper.assertException(FILTER, "text web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "text http://web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "text https://web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(FILTER, "text www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(
        FILTER, "text http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    filterHelper.assertException(
        FILTER, "text https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
  }

  @Test
  public void whitelistTest() {
    filterHelper.assertException(FILTER, "web.de");
    filterHelper.assertException(FILTER, "www.web.de");
    filterHelper.assertException(FILTER, "google.com");
    filterHelper.assertNoException(FILTER, "www.google.com");
    filterHelper.assertNoException(FILTER, "foobar.net");
    filterHelper.assertNoException(FILTER, "www.foobar.net");
    filterHelper.assertException(FILTER, "http://web.de");
    filterHelper.assertException(FILTER, "http://www.web.de");
    filterHelper.assertException(FILTER, "http://google.com");
    filterHelper.assertNoException(FILTER, "http://www.google.com");
    filterHelper.assertNoException(FILTER, "http://foobar.net");
    filterHelper.assertNoException(FILTER, "http://www.foobar.net");
    filterHelper.assertException(FILTER, "https://web.de");
    filterHelper.assertException(FILTER, "https://www.web.de");
    filterHelper.assertException(FILTER, "https://google.com");
    filterHelper.assertNoException(FILTER, "https://www.google.com");
    filterHelper.assertNoException(FILTER, "https://foobar.net");
    filterHelper.assertNoException(FILTER, "https://www.foobar.net");
    filterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "www.google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "foobar.net/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "www.foobar.net/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "http://google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "http://www.google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "http://foobar.net/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "http://www.foobar.net/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertException(FILTER, "https://google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "https://www.google.com/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "https://foobar.net/testUrl.php?bla=baum&foo=bar");
    filterHelper.assertNoException(FILTER, "https://www.foobar.net/testUrl.php?bla=baum&foo=bar");
  }
}
