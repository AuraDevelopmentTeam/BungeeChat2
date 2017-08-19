package dev.aura.bungeechat.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.filter.BlockMessageException;
import dev.aura.bungeechat.api.filter.BungeeChatFilter;
import dev.aura.bungeechat.message.Message;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilterHelper {
    private final Message expectedMessage;

    public void assertException(BungeeChatFilter filter, String text) {
        try {
            filter.applyFilter(AccountManager.getConsoleAccount(), text);

            fail("Expected exception!");
        } catch (ExtendedBlockMessageException e) {
            assertEquals("Exception Message is wrong", expectedMessage, e.getMessageType());
        } catch (BlockMessageException e) {
            fail("ExtendedBlockMessageException expected! (" + e.getMessage() + ')');
        }
    }

    public void assertNoException(BungeeChatFilter filter, String text) {
        try {
            String result = filter.applyFilter(AccountManager.getConsoleAccount(), text);

            assertEquals("Message should not have been filtered", text, result);
        } catch (BlockMessageException e) {
            fail("No exception expected! (" + e.getMessage() + ')');
        }
    }
}
