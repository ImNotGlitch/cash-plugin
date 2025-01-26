package com.github.imnotglitch.listener;

import com.github.imnotglitch.CashPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        CashPlugin.getInstance().getUserCache().load(event.getPlayer());
    }

}
