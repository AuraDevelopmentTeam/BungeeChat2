package dev.aura.bungeechat.message;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class PlaceholdersTest {
  private static final long TIMEOUT = 1000;

  @BeforeClass
  public static void registerPlaceHolders() {
    PlaceHolders.registerPlaceHolders();
  }

  @Test
  public void registerPlaceholdersTest() {
    assertEquals(
        "Placeholders count is incorrect", 56L, PlaceHolderManager.getPlaceholderStream().count());
  }

  @Test(timeout = TIMEOUT)
  public void placeholderMessageEscapeTest() {
    final String message = "Test: &1 %message%";
    final BungeeChatContext context = new BungeeChatContext(message);

    // Note %message% gets replaced with the string "Test %message%"
    assertEquals(
        "Test: &1 Test: &&1 %message%", PlaceHolderManager.processMessage(message, context));
  }
}
