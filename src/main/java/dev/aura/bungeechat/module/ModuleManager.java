package dev.aura.bungeechat.module;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    @Getter
    private static List<Module> moduleList = new ArrayList<>();

    public static String getStringedModuleList() {
        StringBuilder s = new StringBuilder();
        for (Module m : moduleList) {
            s.append(ChatColor.GREEN).append(m.getName()).append(ChatColor.WHITE).append("; ");
        }
        return s.toString();
    }

    public static void registerModule(Module module) { moduleList.add(module); }

    public static void unregisterModule(Module module) { moduleList.remove(module); }

}
