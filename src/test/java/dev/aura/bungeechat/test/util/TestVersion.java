package dev.aura.bungeechat.test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import dev.aura.bungeechat.util.Version;

public class TestVersion {
    @Test
    public void versionsTest() {
        Version ver1_2_3 = new Version("1.2.3");
        Version ver1_2_3_dev = new Version("1.2.3_DEV");
        Version ver1_2_4 = new Version("1.2.4");
        Version ver1_2_4_dev = new Version("1.2.4_DEV");
        Version ver1_3_3 = new Version("1.3.3");
        Version ver1_3_3_dev = new Version("1.3.3_DEV");
        Version ver2_2_3 = new Version("2.2.3");
        Version ver2_2_3_dev = new Version("2.2.3_DEV");
        Version ver1_2_3_4 = new Version("1.2.3.4");
        Version ver1_2_3_4_dev = new Version("1.2.3.4_DEV");
        
        // Test to make sure suffixes don't interfere
        assertEquals(ver1_2_3.compareTo(ver1_2_3_dev), 0);
        assertEquals(ver1_2_4.compareTo(ver1_2_4_dev), 0);
        assertEquals(ver1_3_3.compareTo(ver1_3_3_dev), 0);
        assertEquals(ver2_2_3.compareTo(ver2_2_3_dev), 0);
        assertEquals(ver1_2_3_4.compareTo(ver1_2_3_4_dev), 0);
        
        assertTrue(ver1_2_3.compareTo(ver1_2_4_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver1_3_3_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver2_2_3_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver1_2_3_4_dev) < 0);
    }
}
