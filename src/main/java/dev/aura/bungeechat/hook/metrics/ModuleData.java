package dev.aura.bungeechat.hook.metrics;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bstats.Metrics.SimpleBarChart;

import dev.aura.bungeechat.module.BungeecordModuleManager;

public class ModuleData extends SimpleBarChart {
    public ModuleData() {
        super("modules");
    }

    @Override
    public HashMap<String, Integer> getValues(HashMap<String, Integer> premadeMap) {
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        BungeecordModuleManager.getLocalModules().stream()
                .forEach(module -> sortedMap.put(module.getName(), module.isEnabled() ? 1 : 0));

        sortedMap.put("Servers", 1);

        return sortedMap;
    }
}
