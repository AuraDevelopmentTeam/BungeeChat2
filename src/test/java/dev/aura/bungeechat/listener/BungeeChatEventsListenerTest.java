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
  private MockReceiver mockReceiver;

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
    if (mockReceiver != null) pluginManager.unregisterListener(mockReceiver);

    final Plugin mockPlugin = Mockito.mock(Plugin.class);
    mockReceiver = Mockito.mock(MockReceiver.class);
    pluginManager.registerListener(mockPlugin, mockReceiver);

    listener = new BungeeChatEventsListener();
  }

  private ServerSwitchEvent generateServerSwitchEvent(ProxiedPlayer player) {
    final ServerInfo mockFrom = Mockito.mock(ServerInfo.class);
    return new ServerSwitchEvent(player, mockFrom);
  }

  private PlayerDisconnectEvent generatePlayerDisconnectEvent(ProxiedPlayer player) {
    return new PlayerDisconnectEvent(player);
  }

  private ProxiedPlayer generateProxiedPlayer(UUID uuid) {
    final ProxiedPlayer mockPlayer = Mockito.mock(ProxiedPlayer.class);
    Mockito.when(mockPlayer.getUniqueId()).thenReturn(uuid);
    return mockPlayer;
  }

  private ProxiedPlayer generateProxiedPlayer() {
    return generateProxiedPlayer(UUID.randomUUID());
  }

  @Test
  public void singleJoinTest() {
    final ProxiedPlayer player = generateProxiedPlayer();

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));

    Mockito.verify(mockReceiver).onBungeeChatJoin(Mockito.any());
    Mockito.verify(mockReceiver, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void singleLeaveTest() {
    final ProxiedPlayer player = generateProxiedPlayer();

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player));

    Mockito.verify(mockReceiver).onBungeeChatLeave(Mockito.any());
    Mockito.verify(mockReceiver, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void singleSwitchTest() {
    final ProxiedPlayer player = generateProxiedPlayer();

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player));
    Mockito.verify(mockReceiver).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void doubleJoinInOrderLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer();
    final ProxiedPlayer player2 = generateProxiedPlayer(player1.getUniqueId());

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));

    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockReceiver).onBungeeChatJoin(joinCaptor.capture());
    assertSame(
        joinCaptor.getValue().getPlayer(), player1); // join is fired only on the first player

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockReceiver).onBungeeChatLeave(leaveCaptor.capture());
    assertSame(
        leaveCaptor.getValue().getPlayer(), player1); // leave is fired only on the first player

    Mockito.verify(mockReceiver, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void doubleJoinOutOfOrderLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer();
    final ProxiedPlayer player2 = generateProxiedPlayer(player1.getUniqueId());

    listener.onPlayerServerSwitch(generateServerSwitchEvent(player1));
    listener.onPlayerServerSwitch(generateServerSwitchEvent(player2));

    listener.onPlayerLeave(generatePlayerDisconnectEvent(player1)); // out of order
    listener.onPlayerLeave(generatePlayerDisconnectEvent(player2));

    ArgumentCaptor<BungeeChatJoinEvent> joinCaptor =
        ArgumentCaptor.forClass(BungeeChatJoinEvent.class);
    Mockito.verify(mockReceiver).onBungeeChatJoin(joinCaptor.capture());
    assertSame(
        joinCaptor.getValue().getPlayer(), player1); // join is fired only on the first player

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockReceiver).onBungeeChatLeave(leaveCaptor.capture());
    assertSame(
        leaveCaptor.getValue().getPlayer(), player1); // leave is fired only on the first player

    Mockito.verify(mockReceiver, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  @Test
  public void tripleInterleavedJoinLeaveTest() {
    final ProxiedPlayer player1 = generateProxiedPlayer();
    final ProxiedPlayer player2 = generateProxiedPlayer(player1.getUniqueId());
    final ProxiedPlayer player3 = generateProxiedPlayer(player1.getUniqueId());

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
    Mockito.verify(mockReceiver, Mockito.times(2)).onBungeeChatJoin(joinCaptor.capture());
    List<BungeeChatJoinEvent> joinEvents = joinCaptor.getAllValues();

    assertSame(joinEvents.get(0).getPlayer(), player1);
    assertSame(joinEvents.get(1).getPlayer(), player3);

    ArgumentCaptor<BungeeChatLeaveEvent> leaveCaptor =
        ArgumentCaptor.forClass(BungeeChatLeaveEvent.class);
    Mockito.verify(mockReceiver, Mockito.times(2)).onBungeeChatLeave(leaveCaptor.capture());
    List<BungeeChatLeaveEvent> leaveEvents = leaveCaptor.getAllValues();

    assertSame(leaveEvents.get(0).getPlayer(), player1);
    assertSame(leaveEvents.get(1).getPlayer(), player3);

    Mockito.verify(mockReceiver, Mockito.never()).onBungeeChatServerSwitch(Mockito.any());
  }

  public static class MockReceiver implements Listener {
    @EventHandler
    public void onBungeeChatJoin(BungeeChatJoinEvent e) {}

    @EventHandler
    public void onBungeeChatLeave(BungeeChatLeaveEvent e) {}

    @EventHandler
    public void onBungeeChatServerSwitch(BungeeChatServerSwitchEvent e) {}
  }
}
