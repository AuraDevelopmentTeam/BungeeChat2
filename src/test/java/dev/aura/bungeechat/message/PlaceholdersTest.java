package dev.aura.bungeechat.message;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import org.junit.Test;

public class PlaceholdersTest {
  @Test
  public void registerPlaceholdersTest() {
    PlaceHolders.registerPlaceHolders();

    assertEquals(
        "Placeholders count is incorrect", 53L, PlaceHolderManager.getPlaceholderStream().count());
  }
}
