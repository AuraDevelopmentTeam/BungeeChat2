package dev.aura.bungeechat.test.filter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.AdvertisingFilter;
import dev.aura.bungeechat.message.Message;

public class AdvertisingFilterTest {
    private static BungeeChatFilter FILTER = new AdvertisingFilter(Arrays.asList("www.google.com", "*.net"), true);

    @BeforeClass
    public static void setUpBeforeClass() {
        FilterHelper.setExpectedError(Message.ANTI_ADVERTISE);
    }

    @Test
    public void consoleTest() throws BlockMessageException {
        final BungeeChatFilter filter = new AdvertisingFilter(Arrays.asList());

        FilterHelper.assertNoException(filter, "test");
    }

    @Test
    public void priorityTest() {
        assertEquals("Returned priority is not as expected.", FilterManager.ADVERTISING_FILTER_PRIORITY,
                FILTER.getPriority());
    }

    @Test
    public void urlFormatTest() {
        FilterHelper.assertException(FILTER, "web.de");
        FilterHelper.assertException(FILTER, "http://web.de");
        FilterHelper.assertException(FILTER, "https://web.de");
        FilterHelper.assertException(FILTER, "www.web.de");
        FilterHelper.assertException(FILTER, "http://www.web.de");
        FilterHelper.assertException(FILTER, "https://www.web.de");
        FilterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text web.de");
        FilterHelper.assertException(FILTER, "text http://web.de");
        FilterHelper.assertException(FILTER, "text https://web.de");
        FilterHelper.assertException(FILTER, "text www.web.de");
        FilterHelper.assertException(FILTER, "text http://www.web.de");
        FilterHelper.assertException(FILTER, "text https://www.web.de");
        FilterHelper.assertException(FILTER, "text web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text http://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text https://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text http://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "text https://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "web.de foobar");
        FilterHelper.assertException(FILTER, "http://web.de foobar");
        FilterHelper.assertException(FILTER, "https://web.de foobar");
        FilterHelper.assertException(FILTER, "www.web.de foobar");
        FilterHelper.assertException(FILTER, "http://www.web.de foobar");
        FilterHelper.assertException(FILTER, "https://www.web.de foobar");
        FilterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text web.de foobar");
        FilterHelper.assertException(FILTER, "text http://web.de foobar");
        FilterHelper.assertException(FILTER, "text https://web.de foobar");
        FilterHelper.assertException(FILTER, "text www.web.de foobar");
        FilterHelper.assertException(FILTER, "text http://www.web.de foobar");
        FilterHelper.assertException(FILTER, "text https://www.web.de foobar");
        FilterHelper.assertException(FILTER, "text web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text http://web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text https://web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        FilterHelper.assertException(FILTER, "text https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    }

    @Test
    public void whitelistTest() {
        FilterHelper.assertException(FILTER, "web.de");
        FilterHelper.assertException(FILTER, "www.web.de");
        FilterHelper.assertException(FILTER, "google.com");
        FilterHelper.assertNoException(FILTER, "www.google.com");
        FilterHelper.assertNoException(FILTER, "foobar.net");
        FilterHelper.assertNoException(FILTER, "www.foobar.net");
        FilterHelper.assertException(FILTER, "http://web.de");
        FilterHelper.assertException(FILTER, "http://www.web.de");
        FilterHelper.assertException(FILTER, "http://google.com");
        FilterHelper.assertNoException(FILTER, "http://www.google.com");
        FilterHelper.assertNoException(FILTER, "http://foobar.net");
        FilterHelper.assertNoException(FILTER, "http://www.foobar.net");
        FilterHelper.assertException(FILTER, "https://web.de");
        FilterHelper.assertException(FILTER, "https://www.web.de");
        FilterHelper.assertException(FILTER, "https://google.com");
        FilterHelper.assertNoException(FILTER, "https://www.google.com");
        FilterHelper.assertNoException(FILTER, "https://foobar.net");
        FilterHelper.assertNoException(FILTER, "https://www.foobar.net");
        FilterHelper.assertException(FILTER, "web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "www.google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "foobar.net/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "www.foobar.net/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "http://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "http://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "http://google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "http://www.google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "http://foobar.net/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "http://www.foobar.net/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "https://web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "https://www.web.de/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertException(FILTER, "https://google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "https://www.google.com/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "https://foobar.net/testUrl.php?bla=baum&foo=bar");
        FilterHelper.assertNoException(FILTER, "https://www.foobar.net/testUrl.php?bla=baum&foo=bar");
    }
}
