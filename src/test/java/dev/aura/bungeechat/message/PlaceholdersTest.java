package dev.aura.bungeechat.message;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.message.PlaceHolders;

public class PlaceholdersTest {
    @Test
    public void registerPlaceholdersTest() {
        PlaceHolders.registerPlaceholders();

        assertEquals("Placeholders count is incorrect", 47L, PlaceHolderManager.getPlaceholderStream().count());
    }
}
