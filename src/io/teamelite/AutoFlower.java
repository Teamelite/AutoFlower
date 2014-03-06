package io.teamelite;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoFlower extends JavaPlugin {
    
    public FlowerProducer flowerProducer;
    
    @Override
    public void onEnable() {
        flowerProducer = new FlowerProducer();
    }
    
    @Override
    public void onDisable() {
        
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(formatMessage("Must be instance of player!")); //Locations are required
            return true;
        }
        
        Player player = (Player)sender;
        double chance = 0.7;
        double flowerChance = 0.003;
        
        if(cmd.getName().equalsIgnoreCase("autoflower")) {
            if(args.length > 0) {
                
                int radius = -1;
                try {
                    radius = Integer.valueOf(args[0]);
                    if(args.length > 1) {
                        double in = (double)Math.abs(Integer.valueOf(args[1]));
                        if(in > 100) in = 100;
                        chance = in/100; //Converts to between 0 and 1 for algorithm use
                    } if (args.length > 2) {
                        double in = (double)Math.abs(Integer.valueOf(args[2]));
                        if(in >100) in = 100;
                        flowerChance = in/2200; //Ensures value will at most cause all flowers to have an equal chance to spawn
                    }
                } catch (NumberFormatException e) {
                    if(args.length > 2)
                        player.sendMessage(formatMessage("All arguments must be an integer! [radius] <Chance 0-100> <flowerChance 0-100>"));
                    else if(args.length > 1)
                        player.sendMessage(formatMessage("Both arguments must be an integer! [radius] <Chance 0-100>"));
                    else 
                        player.sendMessage(formatMessage("Argument must be an integer. [radius]"));
                    
                    return true;
                }
                
                radius = Math.abs(radius); //Ensure no negatives. Only positives. :)
                
                flowerProducer.createFlowers(radius, player, chance, flowerChance);
                
                player.sendMessage(formatMessage("Complete!"));
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("clearflower")) {
            if(args.length > 0) {
                
                int radius = -1;
                try {
                    
                    radius = Integer.valueOf(args[0]);
                    
                } catch (NumberFormatException e) { 
                    
                    player.sendMessage(formatMessage("Argument must be an integer. [radius]"));
                    return true;
                }
                
                radius = Math.abs(radius);
                
                flowerProducer.clearFlowers(radius, player);
                
                player.sendMessage(formatMessage("Complete!"));
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("autosnow")) {
            
            if(args.length > 0) {
                
                int radius = -1;
                try {
                    radius = Integer.valueOf(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(formatMessage("Argument must be an integer. [radius]"));
                    return true;
                }
            
                flowerProducer.snowGenerate(radius, player);
                player.sendMessage(formatMessage("Complete!"));
                
                return true;
                
            }
        } else if(cmd.getName().equalsIgnoreCase("clearsnow")) {
            
            if(args.length > 0) {
            
                int radius = -1;
                try {
                    radius = Integer.valueOf(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(formatMessage("Argument must be an integer. [radius]"));
                }
                
                flowerProducer.snowRemove(radius, player);
                player.sendMessage(formatMessage("Complete!"));
                
                return true;
            }
        }
        
        return false;
    }
    
    public String formatMessage(String msg) {
        return ChatColor.DARK_AQUA + "AutoFlower" + ChatColor.AQUA + "> " + msg; //Prettyful
    }
}
