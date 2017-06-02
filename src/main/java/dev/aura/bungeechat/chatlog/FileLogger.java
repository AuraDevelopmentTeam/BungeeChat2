package dev.aura.bungeechat.chatlog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import dev.aura.bungeechat.api.utils.TimeUtils;
import net.md_5.bungee.api.ProxyServer;

public class FileLogger implements ChatLogger, AutoCloseable {
    private File dataFolder;
    private File saveTo;
    private FileWriter fw;
    private PrintWriter pw;
    private Timer timer;

    private static Date getMidnight() {
        Calendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_MONTH, 1);
        Calendar midnight = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DATE), 0, 0);

        return midnight.getTime();
    }

    public FileLogger() {
        dataFolder = new File(ProxyServer.getInstance().getPluginsFolder(), "BungeeChat/LogFiles");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        initLogFile();

        timer = new Timer();
        timer.scheduleAtFixedRate(new LogFileChanger(), getMidnight(), TimeUnit.DAYS.toMillis(1));
    }

    @Override
    public void log(String message) {
        pw.flush();
    }

    @Override
    public void close() throws Exception {
        timer.cancel();

        fw.close();
        pw.close();
    }

    private void initLogFile() {
        try {
            saveTo = new File(dataFolder, TimeUtils.getDate().replace('/', '-') + "-chat.log");

            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            fw = new FileWriter(saveTo, true);
            pw = new PrintWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class LogFileChanger extends TimerTask {
        @Override
        public void run() {
            initLogFile();
        }
    }
}
