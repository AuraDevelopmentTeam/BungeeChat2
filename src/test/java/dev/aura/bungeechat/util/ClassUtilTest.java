package dev.aura.bungeechat.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClassUtilTest {
  @Test
  public void doesClassExistTest() {
    assertTrue(ClassUtil.doesClassExist(ClassUtilTest.class.getName()));
    assertFalse(ClassUtil.doesClassExist("xxx.xxx.xxx.Test"));
  }
}
