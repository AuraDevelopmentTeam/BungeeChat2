package dev.aura.bungeechat.module;

import dev.aura.bungeechat.message.MessagesService;
import java.util.List;
import java.util.stream.Collectors;

public class MulticastChatModule extends Module {
  @Override
  public String getName() {
    return "MulticastChat";
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onEnable() {
    MessagesService.setMultiCastServerGroups(
        getModuleSection().getList("serverLists").stream()
            .map(configValue -> (List<String>) configValue.unwrapped())
            .collect(Collectors.toList()));
  }

  @Override
  public void onDisable() {
    MessagesService.unsetMultiCastServerGroups();
  }
}
