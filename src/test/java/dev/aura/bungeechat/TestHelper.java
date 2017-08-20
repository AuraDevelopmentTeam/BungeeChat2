package dev.aura.bungeechat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import dev.aura.bungeechat.config.Configuration;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ReconnectHandler;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

@UtilityClass
public class TestHelper {
    private static BungeeChat bungeeChat;
    private static ProxyServer proxyServer;
    private static boolean hasInitRun = false;

    @SneakyThrows
    public static void initBungeeChat() {
        if (!hasInitRun) {
            proxyServer = new DummyProxyServer();

            ProxyServer.setInstance(proxyServer);

            bungeeChat = new BungeeChat();
            BungeeChat.instance = bungeeChat;

            Method init = Plugin.class.getDeclaredMethod("init", ProxyServer.class, PluginDescription.class);
            init.setAccessible(true);
            init.invoke(bungeeChat, proxyServer, new PluginDescription());

            hasInitRun = true;
        }

        Configuration.load();
    }

    public static void deinitBungeeChat() throws IOException {
        FileUtils.deleteDirectory(bungeeChat.getConfigFolder());
        bungeeChat.getConfigFolder().mkdirs();
    }

    public static class DummyProxyServer extends ProxyServer {
        @Getter
        private PluginManager pluginManager;
        @Getter
        private File pluginsFolder = new File(System.getProperty("java.io.tmpdir"), "BungeeChatTest");
        @Getter
        private Logger logger = Logger.getLogger("DummyProxyServer");

        protected DummyProxyServer() {
            pluginManager = new PluginManager(this);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getVersion() {
            return null;
        }

        @Override
        public String getTranslation(String name, Object... args) {
            return null;
        }

        @Override
        public Collection<ProxiedPlayer> getPlayers() {
            return null;
        }

        @Override
        public ProxiedPlayer getPlayer(String name) {
            return null;
        }

        @Override
        public ProxiedPlayer getPlayer(UUID uuid) {
            return null;
        }

        @Override
        public Map<String, ServerInfo> getServers() {
            return null;
        }

        @Override
        public ServerInfo getServerInfo(String name) {
            return null;
        }

        @Override
        public ConfigurationAdapter getConfigurationAdapter() {
            return null;
        }

        @Override
        public void setConfigurationAdapter(ConfigurationAdapter adapter) {
            // Nothing
        }

        @Override
        public ReconnectHandler getReconnectHandler() {
            return null;
        }

        @Override
        public void setReconnectHandler(ReconnectHandler handler) {
            // Nothing
        }

        @Override
        public void stop() {
            // Nothing
        }

        @Override
        public void stop(String reason) {
            // Nothing
        }

        @Override
        public void start() throws Exception {
            // Nothing
        }

        @Override
        public void registerChannel(String channel) {
            // Nothing
        }

        @Override
        public void unregisterChannel(String channel) {
            // Nothing
        }

        @Override
        public Collection<String> getChannels() {
            return null;
        }

        @Override
        @Deprecated
        public String getGameVersion() {
            return null;
        }

        @Override
        @Deprecated
        public int getProtocolVersion() {
            return 0;
        }

        @Override
        public ServerInfo constructServerInfo(String name, InetSocketAddress address, String motd, boolean restricted) {
            return null;
        }

        @Override
        public CommandSender getConsole() {
            return null;
        }

        @Override
        public TaskScheduler getScheduler() {
            return null;
        }

        @Override
        public int getOnlineCount() {
            return 0;
        }

        @Override
        @Deprecated
        public void broadcast(String message) {
            // Nothing
        }

        @Override
        public void broadcast(BaseComponent... message) {
            // Nothing
        }

        @Override
        public void broadcast(BaseComponent message) {
            // Nothing
        }

        @Override
        public Collection<String> getDisabledCommands() {
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        public net.md_5.bungee.api.ProxyConfig getConfig() {
            return null;
        }

        @Override
        public Collection<ProxiedPlayer> matchPlayer(String match) {
            return null;
        }

        @Override
        public Title createTitle() {
            return null;
        }
    }
}
