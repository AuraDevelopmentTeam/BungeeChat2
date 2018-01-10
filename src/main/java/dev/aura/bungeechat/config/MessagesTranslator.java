package dev.aura.bungeechat.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;
import dev.aura.bungeechat.message.Message;
import dev.aura.bungeechat.util.FileUtils;
import dev.aura.bungeechat.util.LoggerHelper;
import java.io.File;
import java.util.Optional;

public class MessagesTranslator {
  public static final String DEFAULT_LANGUAGE = "en_US";
  protected static final ConfigParseOptions PARSE_OPTIONS =
      ConfigParseOptions.defaults().setAllowMissing(false).setSyntax(ConfigSyntax.CONF);
  protected static final ConfigRenderOptions RENDER_OPTIONS =
      ConfigRenderOptions.defaults().setOriginComments(false).setJson(false);
  private static final String INHERIT = "inherit";

  private final Config defaultLang;
  private final Config translation;

  private static void copyDefaultLanguageFiles(File dir) {
    FileUtils.copyResourcesRecursively(MessagesTranslator.class.getResource("/lang"), dir);
  }

  public MessagesTranslator(File dir, String language) {
    copyDefaultLanguageFiles(dir);

    defaultLang = loadLanguageConfiguration(dir, DEFAULT_LANGUAGE).get();
    translation = loadLanguage(dir, language).withFallback(defaultLang).resolve();
  }

  public Optional<String> translate(Message message) {
    String path = message.getStringPath();

    if (translation.hasPath(path)) return Optional.of(translation.getString(path));
    else return Optional.empty();
  }

  public String translateWithFallback(Message message) {
    return translate(message).orElse(message.getStringPath());
  }

  private Config loadLanguage(File dir, String language) {
    Config langConfig = loadLanguageConfiguration(dir, language).orElse(defaultLang);

    if (langConfig.hasPath(INHERIT)) {
      String inheritLang = langConfig.getString(INHERIT);

      langConfig = langConfig.withFallback(loadLanguage(dir, inheritLang));
    }

    return langConfig;
  }

  private Optional<Config> loadLanguageConfiguration(File dir, String language) {
    File langaugeFile = new File(dir, language + ".lang");

    try {
      if (langaugeFile.exists())
        return Optional.of(ConfigFactory.parseFile(langaugeFile, PARSE_OPTIONS));
    } catch (Exception e) {
      LoggerHelper.error("Could not load language: " + language, e);
    }

    return Optional.empty();
  }
}
