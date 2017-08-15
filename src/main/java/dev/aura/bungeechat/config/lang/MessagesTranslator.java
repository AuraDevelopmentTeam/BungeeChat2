package dev.aura.bungeechat.config.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import dev.aura.bungeechat.util.FileUtils;
import dev.aura.bungeechat.util.LoggerHelper;
import lombok.experimental.Delegate;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MessagesTranslator {
    private static final String DEFAULT_LANGUAGE = "en_US";
    private static final String INHERIT = "inherit";

    @Delegate
    private final Translation translation;
    private final Configuration defaultLang;

    private static void copyDefaultLanguageFiles(File dir) {
        FileUtils.copyResourcesRecursively(MessagesTranslator.class.getResource("/lang"), dir);
    }

    public MessagesTranslator(File dir, String language) {
        copyDefaultLanguageFiles(dir);

        defaultLang = loadLanguageConfiguration(dir, DEFAULT_LANGUAGE).get();
        translation = loadLanguage(dir, language);
    }

    private Translation loadLanguage(File dir, String language) {
        Configuration langConfig = loadLanguageConfiguration(dir, language).orElse(defaultLang);

        if (langConfig.contains(INHERIT)) {
            String inheritLang = langConfig.getString(INHERIT, DEFAULT_LANGUAGE);

            return new Translation(langConfig, loadLanguage(dir, inheritLang));
        } else
            return new Translation(langConfig);
    }

    private Optional<Configuration> loadLanguageConfiguration(File dir, String language) {
        File langaugeFile = new File(dir, language + ".yml");

        try {
            if (langaugeFile.exists())
                return Optional.of(ConfigurationProvider.getProvider(YamlConfiguration.class)
                        .load(new InputStreamReader(new FileInputStream(langaugeFile), StandardCharsets.UTF_8)));
        } catch (FileNotFoundException e) {
            LoggerHelper.error("Could not load language: " + language);
        } catch (Exception e) {
            LoggerHelper.error("Could not load language: " + language, e);
        }

        return Optional.empty();
    }
}
