package dev.aura.bungeechat.module;

public interface Module {
    public String getName();
    
    public void onEnable();

    public void onDisable();
}
