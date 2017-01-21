package net.rhian.agathe.command.commands;

import org.bukkit.ChatColor;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;

/**
 * Created by 360 on 9/18/2015.
 */
@Command(name = "pforcesave", usage = "/pforcesave", permission = "practice.save")
public class CommandSave implements ICommand{

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Agathe.getTaskAutoSave().run();
        cmdArgs.getSender().sendMessage(ChatColor.GREEN+"Save task forcefully run.");
    }
}
