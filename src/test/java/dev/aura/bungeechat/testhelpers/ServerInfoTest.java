package dev.aura.bungeechat.testhelpers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.LinkedHashMap;
import java.util.Map;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.junit.BeforeClass;
import org.mockito.Mockito;

public abstract class ServerInfoTest {
  @SuppressFBWarnings(
      value = {"MS_PKGPROTECT", "MS_CANNOT_BE_FINAL"},
      justification = "Child classes need access to it.")
  protected static Map<String, ServerInfo> servers;

  @BeforeClass
  public static void setupProxyServer() {
    servers = new LinkedHashMap<>(); // LinkedHashMaps keep insertion order
    final ProxyServer mockProxyServer = Mockito.mock(ProxyServer.class);

    addMockServer("main");
    addMockServer("hub1");
    addMockServer("hub2");
    addMockServer("test");

    Mockito.when(mockProxyServer.getServers()).thenReturn(servers);

    ProxyServer.setInstance(mockProxyServer);
  }

  private static void addMockServer(String serverName) {
    final ServerInfo server = Mockito.mock(ServerInfo.class);

    Mockito.when(server.getName()).thenReturn(serverName);

    servers.put(serverName, server);
  }
}
