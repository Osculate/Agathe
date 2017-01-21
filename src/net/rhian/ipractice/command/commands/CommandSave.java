package net.rhian.ipractice.command.commands;

import org.bukkit.ChatColor;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.command.CmdArgs;
import net.rhian.ipractice.command.Command;
import net.rhian.ipractice.command.ICommand;

/**
 * Created by 360 on 9/18/2015.
 */
@Command(name = "pforcesave", usage = "/pforcesave", permission = "practice.save")
public class CommandSave implements ICommand{

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Practice.getTaskAutoSave().run();
        cmdArgs.getSender().sendMessage(ChatColor.GREEN+"Save task forcefully run.");
    }
}
