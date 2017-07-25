package dev.aura.bungeechat.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.aura.bungeechat.util.Version;

public class VersionTest {
    private static final Version ver1_2_3 = new Version("1.2.3");
    private static final Version ver1_2_3_dev = new Version("1.2.3_DEV");
    private static final Version ver1_2_4 = new Version("1.2.4");
    private static final Version ver1_2_4_dev = new Version("1.2.4_DEV");
    private static final Version ver1_3_3 = new Version("1.3.3");
    private static final Version ver1_3_3_dev = new Version("1.3.3_DEV");
    private static final Version ver2_2_3 = new Version("2.2.3");
    private static final Version ver2_2_3_dev = new Version("2.2.3_DEV");
    private static final Version ver1_2_3_4 = new Version("1.2.3.4");
    private static final Version ver1_2_3_4_dev = new Version("1.2.3.4_DEV");

    @Test
    public void suffixTest() {
        assertEquals(ver1_2_3.compareTo(ver1_2_3_dev), 0);
        assertEquals(ver1_2_4.compareTo(ver1_2_4_dev), 0);
        assertEquals(ver1_3_3.compareTo(ver1_3_3_dev), 0);
        assertEquals(ver2_2_3.compareTo(ver2_2_3_dev), 0);
        assertEquals(ver1_2_3_4.compareTo(ver1_2_3_4_dev), 0);
    }

    @Test
    public void comparisionTest() {
        assertTrue(ver1_2_3.compareTo(ver1_2_4_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver1_3_3_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver2_2_3_dev) < 0);
        assertTrue(ver1_2_3.compareTo(ver1_2_3_4_dev) < 0);
        assertTrue(ver1_2_3_4.compareTo(ver1_2_4_dev) < 0);
        assertTrue(ver1_2_3_4.compareTo(ver1_3_3_dev) < 0);
        assertTrue(ver1_2_3_4.compareTo(ver2_2_3_dev) < 0);
    }

    @Test
    public void reverseSuffixTest() {
        assertEquals(ver1_2_3_dev.compareTo(ver1_2_3), 0);
        assertEquals(ver1_2_4_dev.compareTo(ver1_2_4), 0);
        assertEquals(ver1_3_3_dev.compareTo(ver1_3_3), 0);
        assertEquals(ver2_2_3_dev.compareTo(ver2_2_3), 0);
        assertEquals(ver1_2_3_4_dev.compareTo(ver1_2_3_4), 0);
    }

    @Test
    public void reverseComparisionTest() {
        assertTrue(ver1_2_4.compareTo(ver1_2_3_dev) > 0);
        assertTrue(ver1_3_3.compareTo(ver1_2_3_dev) > 0);
        assertTrue(ver2_2_3.compareTo(ver1_2_3_dev) > 0);
        assertTrue(ver1_2_3_4.compareTo(ver1_2_3_dev) > 0);
        assertTrue(ver1_2_4.compareTo(ver1_2_3_4_dev) > 0);
        assertTrue(ver1_3_3.compareTo(ver1_2_3_4_dev) > 0);
        assertTrue(ver2_2_3.compareTo(ver1_2_3_4_dev) > 0);
    }
}
