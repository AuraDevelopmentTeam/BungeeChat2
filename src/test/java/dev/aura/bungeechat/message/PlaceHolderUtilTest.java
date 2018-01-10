package dev.aura.bungeechat.message;

import static org.junit.Assert.assertEquals;

import net.md_5.bungee.api.ChatColor;
import org.junit.Test;

public class PlaceHolderUtilTest {
  @Test
  public void transformAltColorCodesTest() {
    final String originalColors = "&0&1&2&3&4&5&6&7&8&9&a&b&c&d&e&f&k&l&m&n&o&r";
    final String expectedResult = originalColors.replace('&', ChatColor.COLOR_CHAR);

    assertEquals(
        "Color code transformation is invalid!",
        expectedResult,
        PlaceHolderUtil.transformAltColorCodes(originalColors));
  }
}
