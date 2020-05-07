package dev.aura.bungeechat.api.placeholder;

import static org.junit.Assert.assertEquals;

import lombok.Value;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlaceHolderManagerTest {
  private static final long TIMEOUT = 1000;
  private static final BungeeChatContext EMPTY_CONTEXT = new BungeeChatContext();

  @BeforeClass
  public static void registerPlaceHolders() {
    PlaceHolderManager.registerPlaceholder(
        new HelperPlaceholder("test", "HAIII"),
        new HelperPlaceholder("recursive1", "xxx %test% xxx"),
        new HelperPlaceholder("recursive2", "hihi %recursive1% hihi"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void duplicatePlaceholderTest() {
    final HelperPlaceholder placeholder = new HelperPlaceholder("dummy", null);

    PlaceHolderManager.registerPlaceholder(placeholder, placeholder);
  }

  @Test(timeout = TIMEOUT)
  public void escapeTest() {
    final String message = "Test %% Test";

    assertEquals("Test % Test", PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Test(timeout = TIMEOUT)
  public void hangingPlaceholderTest() {
    final String message = "Test %xxx";

    assertEquals(message, PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Test(timeout = TIMEOUT)
  public void hangingDelimiterTest() {
    final String message = "Test %";

    assertEquals(message, PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Test(timeout = TIMEOUT)
  public void unknownPlaceholderTest() {
    final String message = "Test %xxx% %hi%";

    assertEquals(message, PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Test(timeout = TIMEOUT)
  public void placeholderTest() {
    final String message = "Test %test% Test";

    assertEquals("Test HAIII Test", PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Test(timeout = TIMEOUT)
  public void recusivePlaceholderTest() {
    final String message = "Test %recursive2% Test";

    assertEquals(
        "Test hihi xxx HAIII xxx hihi Test",
        PlaceHolderManager.processMessage(message, EMPTY_CONTEXT));
  }

  @Value
  private static final class HelperPlaceholder implements BungeeChatPlaceHolder {
    private final String name;
    private final String replacement;

    @Override
    public boolean isContextApplicable(BungeeChatContext context) {
      return true;
    }

    @Override
    public String getReplacement(String name, BungeeChatContext context) {
      return replacement;
    }
  }
}
