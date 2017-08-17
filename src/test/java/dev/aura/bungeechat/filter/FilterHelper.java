package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.message.Message;

public class FilterHelper {
    private static String expectedError = "";

    public static void setExpectedError(Message message) {
        expectedError = message.getStringPath();
    }

    public static void assertException(BungeeChatFilter filter, String text) {
        try {
            filter.applyFilter(AccountManager.getConsoleAccount(), text);

            fail("Expected exception!");
        } catch (BlockMessageException e) {
            assertEquals("Exception Message is wrong", expectedError, e.getMessage());
        }
    }

    public static void assertNoException(BungeeChatFilter filter, String text) {
        try {
            String result = filter.applyFilter(AccountManager.getConsoleAccount(), text);

            assertEquals("Message should not have been filtered", text, result);
        } catch (BlockMessageException e) {
            fail("No exception expected! (" + e.getMessage() + ')');
        }
    }
}
