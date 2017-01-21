package net.rhian.agathe.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;
import net.rhian.agathe.match.MatchInventory;

@Command(name = "viewinv", playerOnly = true, minArgs = 1)
public class CommandViewInventory implements ICommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        String id = cmdArgs.getArg(0);
        if(MatchInventory.getMatchInventories().containsKey(id)){

            MatchInventory.getMatchInventory(id).open(p);

        }
        else{
            p.sendMessage(ChatColor.RED+"Inventory by that ID not found.");
        }
    }
}
