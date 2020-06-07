package dev.aura.bungeechat.hook.metrics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import dev.aura.bungeechat.BungeeChat;
import java.io.File;
import org.junit.Test;

public class LanguageDataTest {
  @Test
  public void verifyBuiltinLanguages() {
    final File translationFolder =
        new File(BungeeChat.class.getResource("/assets/" + BungeeChat.ID + "/lang").getPath());

    assertTrue(translationFolder.isDirectory());

    String[] translationFiles = translationFolder.list();

    assertNotNull(translationFiles); // Are you happy now SpotBugs?

    for (String translationFile : translationFiles) {
      final String translationName = translationFile.replace(".lang", "");

      assertTrue("language: " + translationName, LanguageData.isValidLangauge(translationName));
    }
  }

  @Test
  public void randomTranslationsTest() {
    for (String lang :
        new String[] {
          "xx", "xx_XX", "en_XX", "xx_US", "xx_XX_XX", "xx_XX_XX_xx_xx_xx", "gfjdhgkdnhjfgjnj", ""
        }) {
      assertFalse("language: " + lang, LanguageData.isValidLangauge(lang));
    }
  }
}
