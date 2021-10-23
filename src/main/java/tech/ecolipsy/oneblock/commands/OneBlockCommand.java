package tech.ecolipsy.oneblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.ecolipsy.oneblock.OneBlock;

public class OneBlockCommand implements CommandExecutor {
    OneBlock plugin;
    public OneBlockCommand(OneBlock plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("oneblock.admincommands")){
            sender.sendMessage(plugin.formatMessage(plugin.permMessage, 2));
            return false;
        }
        if(args.length < 1){
            sender.sendMessage(plugin.formatMessage(plugin.getArgsMessage(command.getName()), 2));
            return false;
        }
        String subCommand = args[0];
        if(subCommand.equalsIgnoreCase("start")){
            if(plugin.active){
                sender.sendMessage(plugin.formatMessage("OneBlock is currently active, to change the give rate, use /oneblock changerate.", 2));
                return false;
            }
            if(args.length < 2){
                sender.sendMessage(plugin.formatMessage(plugin.getArgsMessage(command.getName()), 2));
                return false;
            }
            if(!plugin.isInteger(args[1])) {
                sender.sendMessage(plugin.formatMessage("Give rate must be a valid integer in seconds.", 2));
                return false;
            }
            plugin.seconds = Integer.parseInt(args[1]);
            if(plugin.seconds > 60){
                sender.sendMessage(plugin.formatMessage("Having the give rate above a minute is not recommended, as it would take long time to give items.", 1));
            }
            plugin.active = true;
            plugin.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Material[] mats = Material.values();
                    for(Player p : plugin.getServer().getOnlinePlayers()){
                        ItemStack pItem = new ItemStack(mats[(int) Math.floor(Math.random()*mats.length)]);
                        p.getInventory().addItem(pItem);
                    }
                }
            }, 0, plugin.secondsToTicks(plugin.seconds));
            sender.sendMessage(plugin.formatMessage("Successfully started OneBlock.", 0));
        } else if(subCommand.equalsIgnoreCase("stop")){
            if(!plugin.active){
                sender.sendMessage(plugin.formatMessage("OneBlock is not currently active.", 2));
                return false;
            }
            plugin.active = false;
            Bukkit.getScheduler().cancelTask(plugin.task);
            plugin.task = 0;
            sender.sendMessage(plugin.formatMessage("Successfully stopped OneBlock.", 0));
        } else if(subCommand.equalsIgnoreCase("help")){
            sender.sendMessage(plugin.formatMessage("All the subcommands of /oneblock are:", 0));
            sender.sendMessage(plugin.formatMessage("start, stop, help", 0));
        } else{
            sender.sendMessage(plugin.formatMessage("Subcommand was not found, try using §2/oneblock help§a.", 2));
            plugin.writeLog(subCommand);
            return false;
        }
        return true;
    }
}
