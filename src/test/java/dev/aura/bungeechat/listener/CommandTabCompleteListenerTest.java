package dev.aura.bungeechat.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dev.aura.bungeechat.command.BaseCommand;
import dev.aura.bungeechat.util.LoggerHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@SuppressFBWarnings(
    value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
    justification = "We want to override methods and are using them only to verify")
@RunWith(PowerMockRunner.class)
@PrepareForTest({PluginManager.class, LoggerHelper.class})
public class CommandTabCompleteListenerTest {
  private static Connection connection1;
  private static Connection connection2;
  private static ProxiedPlayer player1;
  private static ProxiedPlayer player2;

  private static BaseCommand commandFalse;
  private static BaseCommand commandPlayer1;
  private static BaseCommand commandPlayer2;
  private static BaseCommand commandReturn;
  private static BaseCommand commandException;
  private static BaseCommand commandNull;

  private static Map<String, Command> commands;

  private static CommandTabCompleteListener listener;

  @BeforeClass
  public static void setupProxyServer() {
    commands = new HashMap<>();

    final ProxyServer mockProxyServer = Mockito.mock(ProxyServer.class);
    final SocketAddress socket1 = Mockito.mock(SocketAddress.class);
    final SocketAddress socket2 = Mockito.mock(SocketAddress.class);
    connection1 = Mockito.mock(Connection.class);
    connection2 = Mockito.mock(Connection.class);
    player1 = Mockito.mock(ProxiedPlayer.class);
    player2 = Mockito.mock(ProxiedPlayer.class);
    final PluginManager pluginManager = PowerMockito.mock(PluginManager.class);

    commandFalse = mockNoPermissionCommand("false");
    commandPlayer1 =
        mockCommand(
            "player1",
            (player, args) -> (player == player1) ? Arrays.asList(args) : Collections.emptyList());
    commandPlayer2 =
        mockCommand(
            "player2",
            (player, args) -> (player == player2) ? Arrays.asList(args) : Collections.emptyList());
    commandReturn = mockCommand("return", (player, args) -> Arrays.asList(args));
    commandException =
        mockCommand(
            "exception",
            (player, args) -> {
              throw new NullPointerException("test text YOLO");
            });
    commandNull = mockCommand("null", (player, args) -> null);

    Mockito.when(pluginManager.getCommands())
        .thenReturn(Collections.unmodifiableCollection(commands.entrySet()));
    Mockito.when(connection1.getSocketAddress()).thenReturn(socket1);
    Mockito.when(connection2.getSocketAddress()).thenReturn(socket2);
    Mockito.when(player1.getSocketAddress()).thenReturn(socket1);
    Mockito.when(player2.getSocketAddress()).thenReturn(socket2);
    Mockito.when(mockProxyServer.getPlayers()).thenReturn(Arrays.asList(player1, player2));
    Mockito.when(mockProxyServer.getPluginManager()).thenReturn(pluginManager);

    ProxyServer.setInstance(mockProxyServer);

    listener = new CommandTabCompleteListener();
    listener.updateBungeeChatCommands();
  }

  private static BaseCommand mockCommand(String command, TabCompleteFunction tabCompletion) {
    final BaseCommand commandHandler = Mockito.mock(BaseCommand.class, Mockito.RETURNS_SMART_NULLS);

    Mockito.when(commandHandler.tabComplete(Mockito.any(), Mockito.any()))
        .then(
            invocation ->
                tabCompletion.tabComplete(invocation.getArgument(0), invocation.getArgument(1)));
    Mockito.when(commandHandler.hasPermission(Mockito.any())).thenReturn(true);

    commands.put(command, commandHandler);
    return commandHandler;
  }

  private static BaseCommand mockNoPermissionCommand(String command) {
    final BaseCommand commandHandler = Mockito.mock(BaseCommand.class, Mockito.RETURNS_SMART_NULLS);

    Mockito.when(commandHandler.hasPermission(Mockito.any())).thenReturn(false);

    commands.put(command, commandHandler);
    return commandHandler;
  }

  private static TabCompleteEvent generateTabCompleteEvent(String cursor) {
    return generateTabCompleteEvent(connection1, cursor);
  }

  private static TabCompleteEvent generateTabCompleteEvent(Connection connection, String cursor) {
    return new TabCompleteEvent(connection, null, cursor, new LinkedList<>());
  }

  @Test
  public void noCommandTest() {
    final TabCompleteEvent event =
        generateTabCompleteEvent(
            "This is just a message, not a command! Just for the lulz: / (Should be all good)");

    listener.onTabComplete(event);

    assertEquals(Collections.emptyList(), event.getSuggestions());
  }

  @Test
  public void duringCommandTest() {
    final TabCompleteEvent event = generateTabCompleteEvent("/justthecommand");

    listener.onTabComplete(event);

    assertEquals(Collections.emptyList(), event.getSuggestions());
  }

  @Test
  public void unknownCommandTest() {
    final TabCompleteEvent event = generateTabCompleteEvent("/xxx arg");

    listener.onTabComplete(event);

    assertEquals(Collections.emptyList(), event.getSuggestions());
  }

  @Test
  public void commandFalseTest() {
    final TabCompleteEvent event = generateTabCompleteEvent("/false test");

    listener.onTabComplete(event);

    Mockito.verify(commandFalse, Mockito.times(1)).hasPermission(Mockito.any());
    Mockito.verify(commandFalse, Mockito.never()).tabComplete(Mockito.any(), Mockito.any());
    assertEquals(Collections.emptyList(), event.getSuggestions());
  }

  @Test
  public void playerSpecifcTest() {
    final String command1 = "/player1 test";
    final String command2 = "/player2 test2";

    final TabCompleteEvent event1p1 = generateTabCompleteEvent(connection1, command1);
    final TabCompleteEvent event1p2 = generateTabCompleteEvent(connection2, command1);
    final TabCompleteEvent event2p1 = generateTabCompleteEvent(connection1, command2);
    final TabCompleteEvent event2p2 = generateTabCompleteEvent(connection2, command2);

    listener.onTabComplete(event1p1);
    listener.onTabComplete(event1p2);
    listener.onTabComplete(event2p1);
    listener.onTabComplete(event2p2);

    Mockito.verify(commandPlayer1, Mockito.times(2))
        .tabComplete(Mockito.any(), AdditionalMatchers.aryEq(new String[] {"test"}));
    Mockito.verify(commandPlayer2, Mockito.times(2))
        .tabComplete(Mockito.any(), AdditionalMatchers.aryEq(new String[] {"test2"}));
    assertEquals(Arrays.asList("test"), event1p1.getSuggestions());
    assertEquals(Collections.emptyList(), event1p2.getSuggestions());
    assertEquals(Collections.emptyList(), event2p1.getSuggestions());
    assertEquals(Arrays.asList("test2"), event2p2.getSuggestions());
  }

  @Test
  public void multipleArgsTest() {
    final TabCompleteEvent event1 = generateTabCompleteEvent("/return test more args");
    final TabCompleteEvent event2 = generateTabCompleteEvent("/return test2   more  args");

    listener.onTabComplete(event1);
    listener.onTabComplete(event2);

    Mockito.verify(commandReturn, Mockito.times(2)).tabComplete(Mockito.any(), Mockito.any());
    assertEquals(Arrays.asList("test", "more", "args"), event1.getSuggestions());
    assertEquals(Arrays.asList("test2", "", "", "more", "", "args"), event2.getSuggestions());
  }

  @Test
  public void exceptionTest() {
    PowerMockito.mockStatic(LoggerHelper.class);

    final TabCompleteEvent event = generateTabCompleteEvent("/exception test");

    listener.onTabComplete(event);

    Mockito.verify(commandException, Mockito.times(1))
        .tabComplete(Mockito.any(), AdditionalMatchers.aryEq(new String[] {"test"}));
    assertEquals(Collections.emptyList(), event.getSuggestions());

    ArgumentCaptor<Throwable> argument = ArgumentCaptor.forClass(Throwable.class);
    PowerMockito.verifyStatic(LoggerHelper.class);
    LoggerHelper.warning(
        Mockito.eq("Uncaught error during tabcomplete of /exception"), argument.capture());
    assertTrue(argument.getValue() instanceof NullPointerException);
    assertEquals("test text YOLO", argument.getValue().getMessage());
  }

  @Test
  public void nullTest() {
    final TabCompleteEvent event = generateTabCompleteEvent("/null test");

    listener.onTabComplete(event);

    Mockito.verify(commandNull, Mockito.times(1))
        .tabComplete(Mockito.any(), AdditionalMatchers.aryEq(new String[] {"test"}));
    assertEquals(Collections.emptyList(), event.getSuggestions());
  }

  private static interface TabCompleteFunction {
    public Collection<String> tabComplete(CommandSender sender, String args[]);
  }
}
