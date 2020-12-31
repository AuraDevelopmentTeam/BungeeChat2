package dev.aura.bungeechat.api.util;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.api.utils.TimeUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import org.junit.Test;

public class TimeUtilTest {
  private static final double DELTA = 0.0;

  private static void assertCorrectTimeFormat(String timeFormat, Supplier<String> testMethod) {
    SimpleDateFormat sdfDate = new SimpleDateFormat(timeFormat);
    Date now = new Date();

    assertEquals("Expected time format to match", sdfDate.format(now), testMethod.get());
  }

  @Test
  public void convertStringTimeToDoubleTest() {
    assertEquals(
        "Converting years to milliseconds yielded the wrong result.",
        331128000000.0,
        TimeUtil.convertStringTimeToDouble("10.5y"),
        DELTA);
    assertEquals(
        "Converting months to milliseconds yielded the wrong result.",
        27216000000.0,
        TimeUtil.convertStringTimeToDouble("10.5mo"),
        DELTA);
    assertEquals(
        "Converting weeks to milliseconds yielded the wrong result.",
        6350400000.0,
        TimeUtil.convertStringTimeToDouble("10.5w"),
        DELTA);
    assertEquals(
        "Converting days to milliseconds yielded the wrong result.",
        907200000.0,
        TimeUtil.convertStringTimeToDouble("10.5d"),
        DELTA);
    assertEquals(
        "Converting hours to milliseconds yielded the wrong result.",
        37800000.0,
        TimeUtil.convertStringTimeToDouble("10.5h"),
        DELTA);
    assertEquals(
        "Converting minutes to milliseconds yielded the wrong result.",
        630000.0,
        TimeUtil.convertStringTimeToDouble("10.5m"),
        DELTA);
    assertEquals(
        "Converting seconds to milliseconds yielded the wrong result.",
        10500.0,
        TimeUtil.convertStringTimeToDouble("10.5s"),
        DELTA);
    assertEquals(
        "Converting milliseconds to milliseconds yielded the wrong result.",
        10.5,
        TimeUtil.convertStringTimeToDouble("10.5"),
        DELTA);
  }

  @Test(expected = NumberFormatException.class)
  public void convertStringTimeToDoubleExceptionTest1() {
    TimeUtil.convertStringTimeToDouble("10ms");
  }

  @Test(expected = NumberFormatException.class)
  public void convertStringTimeToDoubleExceptionTest2() {
    TimeUtil.convertStringTimeToDouble("10xz");
  }

  @Test
  public void getDateTest() {
    assertCorrectTimeFormat("yyyy/MM/dd", TimeUtil::getDate);
  }

  @Test
  public void getDayTest() {
    assertCorrectTimeFormat("dd", TimeUtil::getDay);
  }

  @Test
  public void getLongTimeStampTest() {
    assertCorrectTimeFormat("yyyy/MM/dd HH:mm:ss", TimeUtil::getLongTimeStamp);
  }

  @Test
  public void getMonthTest() {
    assertCorrectTimeFormat("MM", TimeUtil::getMonth);
  }

  @Test
  public void getShortTimeStampTest() {
    assertCorrectTimeFormat("HH:mm", TimeUtil::getShortTimeStamp);
  }

  @Test
  public void getTimeStampTest() {
    assertCorrectTimeFormat("HH:mm:ss", TimeUtil::getTimeStamp);
  }

  @Test
  public void getYearTest() {
    assertCorrectTimeFormat("yyyy", TimeUtil::getYear);
  }
}
