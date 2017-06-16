package dev.aura.bungeechat.hook.metrics;

import org.bstats.Metrics;

import com.google.gson.JsonObject;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.enums.BuildType;

public final class BungeeChatMetrics extends Metrics {
    protected BungeeChatMetrics(BungeeChat plugin) {
        super(plugin);
    }

    @Override
    public JsonObject getPluginData() {
        JsonObject pluginData = super.getPluginData();

        if (BungeeChatApi.BUILD_TYPE != BuildType.RELEASE) {
            pluginData.addProperty("pluginVersion", BungeeChatApi.VERSION + '_' + BungeeChatApi.BUILD);
        }

        return pluginData;
    }
}
