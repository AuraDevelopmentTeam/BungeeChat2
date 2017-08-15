package dev.aura.bungeechat.config.lang;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.aura.bungeechat.message.Message;
import net.md_5.bungee.config.Configuration;

public class Translation {
    private final Map<Message, String> translationMapping;
    private final Optional<Translation> fallback;

    public Translation(Configuration translation, Optional<Translation> fallback) {
        translationMapping = translation.getKeys().stream().filter(Message::contains)
                .collect(Collectors.toMap(key -> Message.valueOf(key), key -> translation.getString(key)));
        this.fallback = fallback;
    }

    public Translation(Configuration translation, Translation fallback) {
        this(translation, Optional.of(fallback));
    }

    public Translation(Configuration translation) {
        this(translation, Optional.empty());
    }

    public Optional<String> translate(Message message) {
        if (translationMapping.containsKey(message))
            return Optional.of(translationMapping.get(message));
        else if (fallback.isPresent())
            return fallback.get().translate(message);
        else
            return Optional.empty();
    }
}
