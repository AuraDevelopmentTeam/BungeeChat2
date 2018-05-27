package dev.aura.bungeechat.chatlog;

import dev.aura.bungeechat.BungeeChat;
import dev.aura.bungeechat.api.placeholder.BungeeChatContext;
import dev.aura.bungeechat.api.placeholder.PlaceHolderManager;
import dev.aura.bungeechat.message.Format;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileLogger implements ChatLogger, AutoCloseable {
  private static final BungeeChatContext context = new BungeeChatContext();
  private static final File pluginDir = BungeeChat.getInstance().getConfigFolder();

  private final String logFile;
  private String oldFile = "";
  private File saveTo;
  private Writer fw;
  private PrintWriter pw;

  @Override
  public void log(BungeeChatContext context) {
    initLogFile();

    pw.println(Format.CHAT_LOGGING_FILE.get(context));
    pw.flush();
  }

  @Override
  public void close() throws Exception {
    fw.close();
    pw.close();
  }

  private void initLogFile() {
    String newFile = PlaceHolderManager.processMessage(logFile, context);

    if (oldFile.equals(newFile)) return;

    try {
      saveTo = new File(pluginDir, newFile);
      Optional.ofNullable(saveTo.getParentFile()).ifPresent(File::mkdirs);

      if (!saveTo.exists() && !saveTo.createNewFile()) {
        throw new IOException("Could not create " + saveTo);
      }

      fw = new OutputStreamWriter(new FileOutputStream(saveTo, true), StandardCharsets.UTF_8);
      pw = new PrintWriter(fw);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
