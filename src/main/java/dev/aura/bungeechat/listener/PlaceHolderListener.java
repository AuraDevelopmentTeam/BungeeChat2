package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.utils.TimeUtils;
import dev.aura.bungeechat.event.PlaceHolderEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("unused")
public class PlaceHolderListener implements Listener {

    @EventHandler
    public void onPlaceHolerEventCall(PlaceHolderEvent event) {
        event.registerPlaceHolder("%data_time%", TimeUtils.getCurrentTimeStamp());
        event.registerPlaceHolder("%data_day%", TimeUtils.getCurrentDay());
        event.registerPlaceHolder("%data_month%", TimeUtils.getCurrentMonth());
        event.registerPlaceHolder("%data_year%", TimeUtils.getCurrentYear());
        if (event.getTarget() == null) {
            ProxiedPlayer player = event.getSender();
            event.registerPlaceHolder("%player_name%", player.getName());
            event.registerPlaceHolder("%player_displayname%", player.getDisplayName());
            event.registerPlaceHolder("%player_ping%", String.valueOf(player.getPing()));
            event.registerPlaceHolder("%player_uuid%", player.getUniqueId().toString());
            event.registerPlaceHolder("%player_servername%", player.getServer().getInfo().getName());
            event.registerPlaceHolder("%player_serverip%", String.valueOf(player.getServer().getInfo().getAddress()));
        } else {
            ProxiedPlayer sender = event.getSender();
            event.registerPlaceHolder("%sender_name%", sender.getName());
            event.registerPlaceHolder("%sender_displayname%", sender.getDisplayName());
            event.registerPlaceHolder("%sender_ping%", String.valueOf(sender.getPing()));
            event.registerPlaceHolder("%sender_uuid%", sender.getUniqueId().toString());
            event.registerPlaceHolder("%sender_servername%", sender.getServer().getInfo().getName());
            event.registerPlaceHolder("%sender_serverip%", String.valueOf(sender.getServer().getInfo().getAddress()));
            ProxiedPlayer target = event.getTarget();
            event.registerPlaceHolder("%target_name%", target.getName());
            event.registerPlaceHolder("%target_displayname%", target.getDisplayName());
            event.registerPlaceHolder("%target_ping%", String.valueOf(target.getPing()));
            event.registerPlaceHolder("%target_uuid%", target.getUniqueId().toString());
            event.registerPlaceHolder("%target_servername%", target.getServer().getInfo().getName());
            event.registerPlaceHolder("%target_serverip%", String.valueOf(target.getServer().getInfo().getAddress()));
        }
    }

}
