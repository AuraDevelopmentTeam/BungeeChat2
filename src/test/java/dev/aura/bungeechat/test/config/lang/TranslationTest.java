package dev.aura.bungeechat.test.config.lang;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import dev.aura.bungeechat.config.lang.Translation;
import dev.aura.bungeechat.message.Message;
import net.md_5.bungee.config.Configuration;

public class TranslationTest {
    private static Configuration empty;
    private static String firstKey;
    private static Configuration firstKeyMissing;
    private static Configuration oneKey;

    private static String secondKey;
    private static Configuration twoKeys;

    @BeforeClass
    public static void setUpBeforeClass() {
        empty = new Configuration();
        oneKey = new Configuration();
        twoKeys = new Configuration();
        firstKeyMissing = new Configuration();

        firstKey = Message.ADD_IGNORE.getStringPath();
        secondKey = Message.ALREADY_IGNORED.getStringPath();

        oneKey.set(firstKey, "oneKey");

        twoKeys.set(firstKey, "twoKeys");
        twoKeys.set(secondKey, "twoKeys");

        firstKeyMissing.set(secondKey, "firstKeyMissing");
    }

    @Test
    public void basicTest() {
        Translation translation = new Translation(twoKeys);

        assertEquals("Translation should be twoKeys", Optional.of("twoKeys"),
                translation.translate(Message.ADD_IGNORE));
        assertEquals("Translation should be twoKeys", Optional.of("twoKeys"),
                translation.translate(Message.ALREADY_IGNORED));
        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ANTI_ADVERTISE));
    }

    @Test
    public void inheritanceTest() {
        Translation translation = new Translation(oneKey, new Translation(firstKeyMissing));

        assertEquals("Translation should be oneKey", Optional.of("oneKey"), translation.translate(Message.ADD_IGNORE));
        assertEquals("Translation should be firstKeyMissing", Optional.of("firstKeyMissing"),
                translation.translate(Message.ALREADY_IGNORED));
        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ANTI_ADVERTISE));
    }

    @Test
    public void multiInheritanceTest() {
        Translation translation = new Translation(oneKey, new Translation(firstKeyMissing, new Translation(twoKeys)));

        assertEquals("Translation should be oneKey", Optional.of("oneKey"), translation.translate(Message.ADD_IGNORE));
        assertEquals("Translation should be firstKeyMissing", Optional.of("firstKeyMissing"),
                translation.translate(Message.ALREADY_IGNORED));
        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ANTI_ADVERTISE));
    }

    @Test
    public void notFoundTest() {
        Translation translation = new Translation(empty);

        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ADD_IGNORE));
        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ALREADY_IGNORED));
        assertEquals("Translation should be empty", Optional.empty(), translation.translate(Message.ANTI_ADVERTISE));
    }
}
