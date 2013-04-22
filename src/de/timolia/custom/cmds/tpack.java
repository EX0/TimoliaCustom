/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.custom.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class tpack extends TCommand {

    protected void prepare() {
        permission();
        minArgs(1);
        maxArgs(2);
    }

    public void perform(final CommandSender sender, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(prefix + args[0] + " ist nicht online!");
                return;
            }

            try {
                target.setTexturePack(args[1]);
            } catch (Exception e) {
                sender.sendMessage(prefix + "Die URL ist zu lang!");
            }
        } else {
            for (Player p : Bukkit.getOnlinePlayers())
                try {
                    p.setTexturePack(args[0]);
                } catch (Exception e) {
                    sender.sendMessage(prefix + "Die URL ist zu lang!");
                    break;
                }
        }
    }
}