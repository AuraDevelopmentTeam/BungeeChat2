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

import dev.aura.bungeechat.api.utils.TimeUtils;
import net.md_5.bungee.api.ProxyServer;

public class FileLogger implements ChatLogger, AutoCloseable {
    File dataFolder;
    File saveTo;
    FileWriter fw;
    PrintWriter pw;
	
    private final static long fONCE_PER_DAY = 1000*60*60*24;

    private final static int fONE_DAY = 1;
	
	public FileLogger() {
        try {
            dataFolder = new File(ProxyServer.getInstance().getPluginsFolder() + "/BungeeChat/LogFiles");
        	if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
        	String timestamp = TimeUtils.getCurrentYear() + "-" + TimeUtils.getCurrentMonth() + "-" + TimeUtils.getCurrentDay();
	    	
        	saveTo = new File(dataFolder, timestamp + "BungeeChat.log");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
            fw = new FileWriter(saveTo, true);
            pw = new PrintWriter(fw);
            
            TimerTask newFile = newFile();
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(newFile, getMidnight(), fONCE_PER_DAY);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    private TimerTask newFile(){
        try {
            String timestamp = TimeUtils.getCurrentYear() + "-" + TimeUtils.getCurrentMonth() + "-" + TimeUtils.getCurrentDay();
    	
    	    saveTo = new File(dataFolder, timestamp + "BungeeChat.log");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
         
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
    private static Date getMidnight(){
        Calendar midnight = new GregorianCalendar();
        midnight.add(Calendar.DATE, fONE_DAY);
        Calendar result = new GregorianCalendar(
        	midnight.get(Calendar.YEAR),
        	midnight.get(Calendar.MONTH),
        	midnight.get(Calendar.DATE),
            0,
            0
        );
        return result.getTime();
      }

    @Override
    public void log(String message) {
        String logMessage = TimeUtils.getFullCurrentTimeStamp() + message;
        logToFile(logMessage);
    }
    
    public void logToFile(String message) {
        pw.println(message);
        pw.flush();
    }

    @Override
    public void close() throws Exception {
    	fw.close();
        pw.close();
    }
}
