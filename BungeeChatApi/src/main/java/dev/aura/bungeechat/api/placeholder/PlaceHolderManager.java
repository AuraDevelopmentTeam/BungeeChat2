package dev.aura.bungeechat.api.placeholder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaceHolderManager {
  private static final List<BungeeChatPlaceHolder> placeholders = new LinkedList<>();

  public static Stream<BungeeChatPlaceHolder> getPlaceholderStream() {
    return placeholders.stream();
  }

  public static Stream<BungeeChatPlaceHolder> getApplicableStream(BungeeChatContext context) {
    return getPlaceholderStream().filter(placeholder -> placeholder.isContextApplicable(context));
  }

  public static String processMessage(String message, BungeeChatContext context) {
    for (BungeeChatPlaceHolder placeholder :
        getApplicableStream(context).collect(Collectors.toList())) {
      message = placeholder.apply(message, context);
    }

    return message;
  }

  public static void registerPlaceholder(BungeeChatPlaceHolder... placeholder) {
    for (BungeeChatPlaceHolder p : placeholder) {
      registerPlaceholder(p);
    }
  }

  public static void registerPlaceholder(BungeeChatPlaceHolder placeholder) {
    if (placeholders.contains(placeholder))
      throw new IllegalStateException(
          "Placeholder " + placeholder.getName() + " has already been registered!");

    placeholders.add(placeholder);
  }

  public static void clear() {
    placeholders.clear();
  }
}
