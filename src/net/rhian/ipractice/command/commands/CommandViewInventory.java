package net.rhian.ipractice.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.ipractice.command.CmdArgs;
import net.rhian.ipractice.command.Command;
import net.rhian.ipractice.command.ICommand;
import net.rhian.ipractice.match.MatchInventory;

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
