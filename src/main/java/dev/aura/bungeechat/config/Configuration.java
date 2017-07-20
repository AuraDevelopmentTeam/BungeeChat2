package dev.aura.bungeechat.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.util.LoggerHelper;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Configuration implements Config {
	protected static final String CONFIG_FILE_NAME = "config.conf";
	protected static final File CONFIG_FILE = new File(BungeeChat.getInstance().getConfigFolder(), CONFIG_FILE_NAME);
	protected static final ConfigParseOptions PARSE_OPTIONS = ConfigParseOptions.defaults().setAllowMissing(false)
			.setSyntax(ConfigSyntax.CONF);
	protected static final ConfigRenderOptions RENDER_OPTIONS = ConfigRenderOptions.defaults().setOriginComments(false);
	@Getter(value = AccessLevel.PROTECTED, lazy = true)
	private static final String header = loadHeader();

	@Getter
	protected static Configuration currentConfig;

	@Delegate
	protected Config config;

	/**
	 * Creates and loads the config. Also saves it so that all missing values
	 * exist!<br>
	 * Also set currentConfig to this config.
	 *
	 * @return a configuration object, loaded from the config file.
	 */
	public static Configuration get() {
		Configuration config = new Configuration();
		config.load();

		currentConfig = config;

		return config;
	}

	private static String loadHeader() {
		StringBuilder header = new StringBuilder();

		try {
			@Cleanup
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(BungeeChat.getInstance().getResourceAsStream(CONFIG_FILE_NAME)));
			String line = reader.readLine();

			while (line.startsWith("#")) {
				header.append(line).append('\n');

				line = reader.readLine();
			}
		} catch (IOException e) {
			LoggerHelper.error("Error loading file header", e);
		}

		return header.toString();
	}

	protected void load() {
		Config defaultConfig = ConfigFactory.parseReader(
				new InputStreamReader(BungeeChat.getInstance().getResourceAsStream(CONFIG_FILE_NAME)), PARSE_OPTIONS);

		if (CONFIG_FILE.exists()) {
			Config fileConfig = ConfigFactory.parseFile(CONFIG_FILE, PARSE_OPTIONS);

			config = fileConfig.withFallback(defaultConfig);
		} else {
			config = defaultConfig;
		}

		config = config.resolve();
		save();
	}

	protected void save() {
		try {
			@Cleanup
			PrintWriter writer = new PrintWriter(CONFIG_FILE, "UTF-8");
			String renderedConfig = config.root().render(RENDER_OPTIONS);
			renderedConfig = getHeader() + renderedConfig;

			writer.print(renderedConfig);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			LoggerHelper.error("Something very unexpected happend! Please report this!", e);
		}
	}
}
