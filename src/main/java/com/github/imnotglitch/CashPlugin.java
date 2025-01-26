package com.github.imnotglitch;

import com.github.imnotglitch.cache.UserCache;
import com.github.imnotglitch.commands.CashCommand;
import com.github.imnotglitch.database.Repository;
import com.github.imnotglitch.listener.PlayerEvent;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.plugin.java.JavaPlugin;

public class CashPlugin extends JavaPlugin {

    private Repository userRepository;
    @Getter
    private UserCache userCache;

    @Getter
    private static CashPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        userRepository = new Repository(
                this.getConfig().getString("mysql.host"),
                this.getConfig().getInt("mysql.port"),
                this.getConfig().getString("mysql.database"),
                this.getConfig().getString("mysql.user"),
                this.getConfig().getString("mysql.password")
        );
        userRepository.init();
        userCache = new UserCache(userRepository);
        Commands();
        getServer().getPluginManager().registerEvents(new PlayerEvent(), this);

    }

    public void Commands() {
        BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(new CashCommand());
    }

    @Override
    public void onDisable() {
        userRepository.closeConnection();
    }
}