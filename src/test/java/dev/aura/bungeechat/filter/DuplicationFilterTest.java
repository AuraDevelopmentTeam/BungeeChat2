package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.api.filter.FilterManager;
import dev.aura.bungeechat.filter.DuplicationFilter;
import dev.aura.bungeechat.message.Message;

public class DuplicationFilterTest {
    private static BungeeChatFilter FILTER = new DuplicationFilter(2, true);

    @BeforeClass
    public static void setUpBeforeClass() {
        FilterHelper.setExpectedError(Message.ANTI_DUPLICATION);
    }

    @Test
    public void complexTest() {
        FilterHelper.assertNoException(FILTER, "test1");
        FilterHelper.assertNoException(FILTER, "test2");
        FilterHelper.assertException(FILTER, "test1");
        FilterHelper.assertException(FILTER, "test2");
        FilterHelper.assertNoException(FILTER, "test3");
        FilterHelper.assertException(FILTER, "test2");
        FilterHelper.assertNoException(FILTER, "test1");
        FilterHelper.assertNoException(FILTER, "test2");
    }

    @Test
    public void consoleTest() throws BlockMessageException {
        final BungeeChatFilter filter = new DuplicationFilter(0);

        FilterHelper.assertNoException(filter, "test");
    }

    @Test
    public void priorityTest() {
        assertEquals("Returned priority is not as expected.", FilterManager.DUPLICATION_FILTER_PRIORITY,
                FILTER.getPriority());
    }

    @Test
    public void simpleTest() {
        FilterHelper.assertNoException(FILTER, "test");
        FilterHelper.assertException(FILTER, "test");
    }
}
