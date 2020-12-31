package dev.aura.bungeechat.listener;

import static org.junit.Assert.assertSame;

import dev.aura.bungeechat.event.BungeeChatJoinEvent;
import dev.aura.bungeechat.event.BungeeChatLeaveEvent;
import dev.aura.bungeechat.event.BungeeChatServerSwitchEvent;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class BungeeChatEventsListenerTest {
  private static PluginManager pluginManager;
  private MockEventListener mockEventListener;

  private BungeeChatEventsListener listener;

  @BeforeClass
  public static void setupProxyServer() {
    final ProxyServer mockProxyServer = Mockito.mock(ProxyServer.class);

    pluginManager = new PluginManager(mockProxyServer);
    Mockito.when(mockProxyServer.getPluginManager()).thenReturn(pluginManager);

    ProxyServer.setInstance(mockProxyServer);
  }

  @Before
  public void setupReceiverAndListener() {
    if (mockEventListener != null) pluginManager.unregisterListener(mockEventListener);

    final Plugin mockPlugin = Mockito.mock(Plugin.class);
    mockEventListener = Mockito.mock(MockEventListener.class);
    pluginManager.registerListener(mockPlugin, mockEventListener);

    listener = new BungeeChatEventsListener();
  }

  private ServerSwitchEvent generateServerSwitchEvent(ProxiedPlayer player) {
    final ServerInfo mockFrom = Mockito.mock(ServerInfo.class);
    return new ServerSwitchEvent(player, mockFrom);
  }

  private PlayerDisconnectEvent generatePlayerDisconnectEvent(ProxiedPlayer player) {
    return new PlayerDisconnectEvent(player);
  }

  private ProxiedPlayer generateProxiedPlayer(String name, UUID uuid) {
    final ProxiedPlayer mockPlayer = Mockito.mock(ProxiedPlayer.class, name);
    Mockito.when(mockPlayer.getUniqueId()).thenReturn(uuid);
    return mockPlayer;
  }

  private ProxiedPlayer generateProxiedPlayer(String name) {
    return generateProxiedPlayer(name, UUID.randomUUID());
  }

  @Test
  public void singleJoinTest() {
    final ProxiedPlayer player = generateProxiedPlayer("player");

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));

    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatJoin(Mockito.any());
    Mockito.verify(mockEventListener, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void singleLeaveTest() {
    final ProxiedPlayer player = generateProxiedPlayer("player");

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player));

    Mockito.verify(mockEventListener, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatLeave(Mockito.any());
  }

  @Test
  public void singleSwitchTest() {
    final ProxiedPlayer player = generateProxiedPlayer("player");

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));

    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatJoin(Mockito.any());
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void doubleJoinInOrderLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer("player1");
    final ProxiedPlayer player2 = generateProxiedPlayer("player2", player1.getUniqueId());

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));

    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatJoin(joinCaptor.capture());
    // join is fired only on the first player
    assertSame(player1, joinCaptor.getValue().getPlayer());

    Mockito.verify(mockEventListener, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatLeave(leaveCaptor.capture());
    // leave is fired only on the first player
    assertSame(player1, leaveCaptor.getValue().getPlayer());
  }

  @Test
  public void doubleJoinOutOfOrderLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer("player1");
    final ProxiedPlayer player2 = generateProxiedPlayer("player2", player1.getUniqueId());

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));

    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1)); // out of order
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatJoin(joinCaptor.capture());
    // join is fired only on the first player
    assertSame(player1, joinCaptor.getValue().getPlayer());

    Mockito.verify(mockEventListener, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(1)).onBungeeChatLeave(leaveCaptor.capture());
    // leave is fired only on the first player
    assertSame(player1, leaveCaptor.getValue().getPlayer());
  }

  @Test
  public void tripleInterleavedJoinLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer("player1");
    final ProxiedPlayer player2 = generateProxiedPlayer("player2", player1.getUniqueId());
    final ProxiedPlayer player3 = generateProxiedPlayer("player3", player1.getUniqueId());

    // player1 | J-L
    // player2 |  J--L
    // player3 |    J-L

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player3));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player3));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(2)).onBungeeChatJoin(joinCaptor.capture());
    List<BungeeChatJoinEvent> joinEvents = joinCaptor.getAllValues();

    assertSame(player1, joinEvents.get(0).getPlayer());
    assertSame(player3, joinEvents.get(1).getPlayer());

    Mockito.verify(mockEventListener, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(2)).onBungeeChatLeave(leaveCaptor.capture());
    List<BungeeChatLeaveEvent> leaveEvents = leaveCaptor.getAllValues();

    assertSame(player1, leaveEvents.get(0).getPlayer());
    assertSame(player3, leaveEvents.get(1).getPlayer());
  }

  @Test
  public void tripleInterleavedJoinSwitchLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer("player1");
    final ProxiedPlayer player2 = generateProxiedPlayer("player2", player1.getUniqueId());
    final ProxiedPlayer player3 = generateProxiedPlayer("player3", player1.getUniqueId());

    // player1 | JS--S-L
    // player2 |   JS-S---SL
    // player3 |        JS--SL

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player3));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player3));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player3));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player3));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(2)).onBungeeChatJoin(joinCaptor.capture());
    List<BungeeChatJoinEvent> joinEvents = joinCaptor.getAllValues();

    assertSame(player1, joinEvents.get(0).getPlayer());
    assertSame(player3, joinEvents.get(1).getPlayer());

    ArgumentCaptor<BungeeChatServerSwitchEvent> switchCaptor =
        ArgumentCaptor.forClass(BungeeChatServerSwitchEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(4))
        .onBungeeChatServerSwitch(switchCaptor.capture());
    List<BungeeChatServerSwitchEvent> switchEvents = switchCaptor.getAllValues();

    assertSame(player1, switchEvents.get(0).getPlayer());
    assertSame(player1, switchEvents.get(1).getPlayer());
    assertSame(player3, switchEvents.get(2).getPlayer());
    assertSame(player3, switchEvents.get(3).getPlayer());

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockEventListener, Mockito.times(2)).onBungeeChatLeave(leaveCaptor.capture());
    List<BungeeChatLeaveEvent> leaveEvents = leaveCaptor.getAllValues();

    assertSame(player1, leaveEvents.get(0).getPlayer());
    assertSame(player3, leaveEvents.get(1).getPlayer());
  }

  @SuppressWarnings("EmptyMethod")
  private static class MockEventListener implements Listener {
    @EventHandler
    public void onBungeeChatJoin(BungeeChatJoinEvent e) {}

    @EventHandler
    public void onBungeeChatLeave(BungeeChatLeaveEvent e) {}

    @EventHandler
    public void onBungeeChatServerSwitch(BungeeChatServerSwitchEvent e) {}
  }
}
