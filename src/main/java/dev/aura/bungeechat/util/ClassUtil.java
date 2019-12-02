package dev.aura.bungeechat.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ClassUtil {
  public static boolean doesClassExist(String className) {
    try {
      Class.forName(className);

      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
