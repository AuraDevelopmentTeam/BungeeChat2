package dev.aura.bungeechat.api.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.InvalidContextError;
import dev.aura.bungeechat.api.utils.BungeeChatInstanceHolder;
import java.io.File;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.BeforeClass;
import org.junit.Test;

public class FilterManagerTest {
  @BeforeClass
  public static void setupApi() {
    BungeeChatInstanceHolder.setInstance(
        new BungeeChatApi() {
          @Override
          public void sendPrivateMessage(BungeeChatContext context) throws InvalidContextError {
            // Nothing
          }

          @Override
          public void sendChannelMessage(BungeeChatContext context, ChannelType channel)
              throws InvalidContextError {
            // Nothing
          }

          @Override
          public File getConfigFolder() {
            return null;
          }
        });
  }

  @Test
  public void executionTest() {
    final String message = "test";

    BungeeChatFilter filter = new TestFilter(message);

    try {
      FilterManager.addFilter(message, filter);

      FilterManager.applyFilters(null, null);

      fail("Filter has not be called!");
    } catch (BlockMessageException e) {
      assertEquals("Exception message not as expected!", message, e.getMessage());
    } finally {
      FilterManager.removeFilter(message);
    }
  }

  @Test
  public void orderTest() throws BlockMessageException {
    final String message1 = "test100";
    final String message2 = "test200";
    final String message3 = "test300";
    final String message4 = "test400";

    BungeeChatFilter filter1 = new FunctionFilter(in -> in + '1', 100);
    BungeeChatFilter filter2 = new FunctionFilter(in -> in + '2', 200);
    BungeeChatFilter filter3 = new FunctionFilter(in -> in + '3', 300);
    BungeeChatFilter filter4 = new FunctionFilter(in -> in + '4', 400);

    try {
      FilterManager.addFilter(message1, filter1);
      FilterManager.addFilter(message2, filter2);
      FilterManager.addFilter(message3, filter3);
      FilterManager.addFilter(message4, filter4);

      String ret = FilterManager.applyFilters(null, "Test_");

      assertEquals("Result message not as expected!", "Test_4321", ret);
    } finally {
      FilterManager.removeFilter(message1);
      FilterManager.removeFilter(message2);
      FilterManager.removeFilter(message3);
      FilterManager.removeFilter(message4);
    }
  }

  @Test
  public void priorityTest() {
    final String message1 = "test100";
    final String message2 = "test200";
    final String message3 = "test300";
    final String message4 = "test400";

    BungeeChatFilter filter1 = new TestFilter(message1, 100);
    BungeeChatFilter filter2 = new TestFilter(message2, 200);
    BungeeChatFilter filter3 = new TestFilter(message3, 300);
    BungeeChatFilter filter4 = new TestFilter(message4, 400);

    try {
      FilterManager.addFilter(message1, filter1);
      FilterManager.addFilter(message2, filter2);
      FilterManager.addFilter(message3, filter3);
      FilterManager.addFilter(message4, filter4);

      FilterManager.applyFilters(null, null);

      fail("Filter has not be called!");
    } catch (BlockMessageException e) {
      assertEquals("Exception message not as expected!", message4, e.getMessage());
    } finally {
      FilterManager.removeFilter(message1);
      FilterManager.removeFilter(message2);
      FilterManager.removeFilter(message3);
      FilterManager.removeFilter(message4);
    }
  }

  @RequiredArgsConstructor
  @Getter
  private static class TestFilter implements BungeeChatFilter {
    private final String message;
    private final int priority;

    public TestFilter(String message) {
      this(message, 0);
    }

    @Override
    public String applyFilter(BungeeChatAccount sender, String message)
        throws BlockMessageException {
      throw new BlockMessageException(this.message);
    }
  }
}
