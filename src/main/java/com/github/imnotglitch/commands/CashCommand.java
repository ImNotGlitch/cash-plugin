package com.github.imnotglitch.commands;

import com.github.imnotglitch.CashPlugin;
import com.github.imnotglitch.cache.User;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.annotation.Optional;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CashCommand {
    @Command(
            name = "cash",
            target = CommandTarget.PLAYER
    )
    public void handleCashCommand(Context<Player> context, @Optional String target) {
        Player player = context.getSender();
        String playerName = (target != null) ? target : player.getName();
        User user = CashPlugin.getInstance().getUserCache().get(playerName);
        int cash = user != null ? user.getCash() : 0;

        if (target != null) {
            player.sendMessage("§aO jogador §e" + playerName + "§a possui §e" + cash + "§a de cash.");
        } else {
            player.sendMessage("§aVocê possui §e" + cash + "§a de cash.");
        }
    }



    @Command(
            name = "cash.set",
            permission = "cash.set",
            usage = "cash set <player> <amount>",
            target = CommandTarget.PLAYER
    )

    public void handleCashSetCommand(Context<Player> context, String target, int quantidade) {
        Player targetPlayer = Bukkit.getPlayerExact(target);
        Player player = context.getSender();

        if (targetPlayer == null) {
            player.sendMessage("§cJogador não encontrado.");
            return;
        }
        CashPlugin.getInstance().getUserCache().setCash(targetPlayer.getName(), quantidade);
        player.sendMessage("§aVocê definiu §e" + quantidade + "§a de cash para §e" + targetPlayer.getName() + "§a.");
    }


    @Command(
            name = "cash.give",
            permission = "cash.give",
            usage = "cash give <player> <amount>",
            target = CommandTarget.PLAYER
    )

    public void handleCashGiveCommand(Context<Player> context, String target, int quantidade) {
        Player targetPlayer = Bukkit.getPlayerExact(target);
        Player player = context.getSender();

        if (targetPlayer == null) {
            player.sendMessage("§cJogador não encontrado.");
            return;
        }
        CashPlugin.getInstance().getUserCache().addCash(targetPlayer.getName(), quantidade);
        player.sendMessage("§aVocê adicionou §e" + quantidade + "§a de cash para §e" + targetPlayer.getName() + "§a.");
    }



}