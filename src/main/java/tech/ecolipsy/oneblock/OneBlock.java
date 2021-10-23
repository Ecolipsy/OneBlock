package tech.ecolipsy.oneblock;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.ecolipsy.oneblock.commands.OneBlockCommand;

public final class OneBlock extends JavaPlugin {
    public String permMessage = "You don't have the required permissions to run this command.";
    public boolean active = false;
    public int seconds = 0;
    public int task;

    public String getArgsMessage(String commandName){
        return "This command required arguments you have not specified, do /" + commandName + " help to find list the required aguments.";
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public String formatMessage(String message, int mode){
        if(mode < 0) {
            mode = 0;
        }
        if(mode > 2) {
            mode = 0;
        }
        String color;
        String p = "§8";
        String s = "§7";
        if(mode == 0){
            p = "§2";
            s = "§a";
        } else if(mode == 1){
            p = "§6";
            s = "§e";
        } else if(mode == 2){
            p = "§4";
            s = "§c";
        }
        return p + "{" + s + "OneBlock" + p + "} " + s + message;
    }

    public void writeLog(String message){
        System.out.println("§2{§aOneBlock§2} §a" + message);
    }

    public int secondsToTicks(int seconds){
        return seconds*20;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        writeLog("OneBlock by Ecolipsy has loaded.");
        writeLog("To start run §2/oneblock start <item give rate in seconds>§a.");
        writeLog("To stop it again, run §2/oneblock stop§a.");
        getCommand("oneblock").setExecutor(new OneBlockCommand(this));
    }
}
