package dev.aura.bungeechat.util;

import static org.junit.Assert.assertEquals;

import dev.aura.bungeechat.testhelpers.ServerInfoTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;

public class ServerNameHelperTest extends ServerInfoTest {
  @Test
  public void getServerInfoTest() {
    assertEquals(Optional.empty(), ServerNameHelper.getServerInfo("h"));
    assertEquals(Optional.empty(), ServerNameHelper.getServerInfo("xxx"));
    assertEquals(Optional.empty(), ServerNameHelper.getServerInfo("mai"));
    assertEquals(Optional.of(servers.get("main")), ServerNameHelper.getServerInfo("main"));
  }

  @Test
  public void verifyServerNameStringTest() {
    assertEquals(Optional.empty(), ServerNameHelper.verifyServerName("h"));
    assertEquals(Optional.empty(), ServerNameHelper.verifyServerName("xxx"));
    assertEquals(Optional.empty(), ServerNameHelper.verifyServerName("mai"));
    assertEquals(Optional.of("main"), ServerNameHelper.verifyServerName("main"));
  }

  @Test
  public void getServerNamesTest() {
    assertEquals(Arrays.asList("main", "hub1", "hub2", "test"), ServerNameHelper.getServerNames());
  }

  @Test
  public void getMatchingServerNamesTest() {
    assertEquals(
        Arrays.asList("main", "hub1", "hub2", "test"), ServerNameHelper.getMatchingServerNames(""));
    assertEquals(Arrays.asList("hub1", "hub2"), ServerNameHelper.getMatchingServerNames("h"));
    assertEquals(Arrays.asList("hub1", "hub2"), ServerNameHelper.getMatchingServerNames("hub"));
    assertEquals(Arrays.asList("hub1"), ServerNameHelper.getMatchingServerNames("hub1"));
    assertEquals(Arrays.asList(), ServerNameHelper.getMatchingServerNames("hub3"));
    assertEquals(Arrays.asList("main"), ServerNameHelper.getMatchingServerNames("main"));
  }
}
