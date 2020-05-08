package dev.aura.bungeechat.util;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;

public class ServerNameUtilTest extends ServerInfoTest {
  @Test
  public void getServerInfoTest() {
    assertEquals(Optional.empty(), ServerNameUtil.getServerInfo("h"));
    assertEquals(Optional.empty(), ServerNameUtil.getServerInfo("xxx"));
    assertEquals(Optional.empty(), ServerNameUtil.getServerInfo("mai"));
    assertEquals(Optional.of(servers.get("main")), ServerNameUtil.getServerInfo("main"));
  }

  @Test
  public void verifyServerNameStringTest() {
    assertEquals(Optional.empty(), ServerNameUtil.verifyServerName("h"));
    assertEquals(Optional.empty(), ServerNameUtil.verifyServerName("xxx"));
    assertEquals(Optional.empty(), ServerNameUtil.verifyServerName("mai"));
    assertEquals(Optional.of("main"), ServerNameUtil.verifyServerName("main"));
  }

  @Test
  public void getServerNamesTest() {
    assertEquals(Arrays.asList("main", "hub1", "hub2", "test"), ServerNameUtil.getServerNames());
  }

  @Test
  public void getMatchingServerNamesTest() {
    assertEquals(
        Arrays.asList("main", "hub1", "hub2", "test"), ServerNameUtil.getMatchingServerNames(""));
    assertEquals(Arrays.asList("hub1", "hub2"), ServerNameUtil.getMatchingServerNames("h"));
    assertEquals(Arrays.asList("hub1", "hub2"), ServerNameUtil.getMatchingServerNames("hub"));
    assertEquals(Arrays.asList("hub1"), ServerNameUtil.getMatchingServerNames("hub1"));
    assertEquals(Arrays.asList(), ServerNameUtil.getMatchingServerNames("hub3"));
    assertEquals(Arrays.asList("main"), ServerNameUtil.getMatchingServerNames("main"));
  }
}
