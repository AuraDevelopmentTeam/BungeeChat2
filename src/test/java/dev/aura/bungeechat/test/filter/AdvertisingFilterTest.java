package dev.aura.bungeechat.test.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;

import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.filter.AdvertisingFilter;
import dev.aura.bungeechat.message.Message;

public class AdvertisingFilterTest {
    private static BungeeChatFilter FILTER = new AdvertisingFilter(Arrays.asList("www.google.com", "*.net"), true);

    private static void expectException(String text) {
        try {
            FILTER.applyFilter(null, text);

            fail("Expected exception!");
        } catch (BlockMessageException e) {
            assertEquals("Exception Message is wrong", Message.NO_PERMISSION.getStringPath(), e.getMessage());
        }
    }

    private static void expectNoException(String text) {
        try {
            FILTER.applyFilter(null, text);
        } catch (BlockMessageException e) {
            fail("No exception expected! (" + e.getMessage() + ')');
        }
    }

    @Test
    public void urlFormatTest() {
        expectException("web.de");
        expectException("http://web.de");
        expectException("https://web.de");
        expectException("www.web.de");
        expectException("http://www.web.de");
        expectException("https://www.web.de");
        expectException("web.de/testUrl.php?bla=baum&foo=bar");
        expectException("http://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("https://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("http://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("https://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text web.de");
        expectException("text http://web.de");
        expectException("text https://web.de");
        expectException("text www.web.de");
        expectException("text http://www.web.de");
        expectException("text https://www.web.de");
        expectException("text web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text http://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text https://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text http://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("text https://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("web.de foobar");
        expectException("http://web.de foobar");
        expectException("https://web.de foobar");
        expectException("www.web.de foobar");
        expectException("http://www.web.de foobar");
        expectException("https://www.web.de foobar");
        expectException("web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("http://web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("https://web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text web.de foobar");
        expectException("text http://web.de foobar");
        expectException("text https://web.de foobar");
        expectException("text www.web.de foobar");
        expectException("text http://www.web.de foobar");
        expectException("text https://www.web.de foobar");
        expectException("text web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text http://web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text https://web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text http://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
        expectException("text https://www.web.de/testUrl.php?bla=baum&foo=bar foobar");
    }

    @Test
    public void whitelistTest() {
        expectException("web.de");
        expectException("www.web.de");
        expectException("google.com");
        expectNoException("www.google.com");
        expectNoException("foobar.net");
        expectNoException("www.foobar.net");
        expectException("http://web.de");
        expectException("http://www.web.de");
        expectException("http://google.com");
        expectNoException("http://www.google.com");
        expectNoException("http://foobar.net");
        expectNoException("http://www.foobar.net");
        expectException("https://web.de");
        expectException("https://www.web.de");
        expectException("https://google.com");
        expectNoException("https://www.google.com");
        expectNoException("https://foobar.net");
        expectNoException("https://www.foobar.net");
        expectException("web.de/testUrl.php?bla=baum&foo=bar");
        expectException("www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("www.google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("foobar.net/testUrl.php?bla=baum&foo=bar");
        expectNoException("www.foobar.net/testUrl.php?bla=baum&foo=bar");
        expectException("http://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("http://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("http://google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("http://www.google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("http://foobar.net/testUrl.php?bla=baum&foo=bar");
        expectNoException("http://www.foobar.net/testUrl.php?bla=baum&foo=bar");
        expectException("https://web.de/testUrl.php?bla=baum&foo=bar");
        expectException("https://www.web.de/testUrl.php?bla=baum&foo=bar");
        expectException("https://google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("https://www.google.com/testUrl.php?bla=baum&foo=bar");
        expectNoException("https://foobar.net/testUrl.php?bla=baum&foo=bar");
        expectNoException("https://www.foobar.net/testUrl.php?bla=baum&foo=bar");
    }
}
