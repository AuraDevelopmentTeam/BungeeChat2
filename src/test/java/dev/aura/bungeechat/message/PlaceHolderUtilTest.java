package dev.aura.bungeechat.message;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PlaceHolderUtilTest {
  @Test
  public void transformAltColorCodesTest() {
    final String originalColors = "&0&1&2&3&4&5&6&7&8&9&a&b&c&d&e&f&k&l&m&n&o&r";
    final String expectedResult = "§0§1§2§3§4§5§6§7§8§9§a§b§c§d§e§f§k§l§m§n§o§r";

    assertEquals(
        "Color code transformation is invalid!",
        expectedResult,
        PlaceHolderUtil.transformAltColorCodes(originalColors));
  }

  @Test
  public void escapeAltColorCodesTest() {
    final String originalMessage = "&0&1&2&3&4&5&6&7&8&9&a&b&c&d&e&f&k&l&m&n&o&r";
    final String expectedResult =
        "&&0&&1&&2&&3&&4&&5&&6&&7&&8&&9&&a&&b&&c&&d&&e&&f&&k&&l&&m&&n&&o&&r";

    assertEquals(
        "Color codes escaping is invalid!",
        expectedResult,
        PlaceHolderUtil.escapeAltColorCodes(originalMessage));
  }

  @Test
  public void escapePlaceholdersTest() {
    final String originalMessage = "%message%%prefix%";
    final String expectedResult = "%%message%%%%prefix%%";

    assertEquals(
        "Placeholder escaping is invalid!",
        expectedResult,
        PlaceHolderUtil.escapePlaceholders(originalMessage));
  }

  @Test
  public void escapeTest() {
    final String originalMessage = "&1%message%&2%prefix%&3";
    final String expectedResult = "&&1%%message%%&&2%%prefix%%&&3";

    assertEquals("Escaping is invalid!", expectedResult, PlaceHolderUtil.escape(originalMessage));
  }
}
