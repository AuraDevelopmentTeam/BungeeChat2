package dev.aura.bungeechat.hook.metrics;

import java.util.HashMap;

import org.bstats.Metrics.SimpleBarChart;

import dev.aura.bungeechat.module.BungeecordModuleManager;

public class ModuleData extends SimpleBarChart {
    public ModuleData() {
        super("modules");
    }

    @Override
    public HashMap<String, Integer> getValues(HashMap<String, Integer> premadeMap) {
        BungeecordModuleManager.getLocalModules().stream().forEach(module -> premadeMap.put(module.getName(), module.isEnabled()? 1 : 0));
        
        return premadeMap;
    }
}
