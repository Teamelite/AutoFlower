package io.teamelite;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R1.block.CraftBlock;
import org.bukkit.entity.Player;

public class FlowerProducer {
    
    public void createFlowers(int radius, Player player, double chance, double flowerChance) {
        
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ(); //Player Positions
        
        World playerWorld = player.getWorld(); //Player's active world
        
        for(int x = -radius; x < radius; x++) { //Loop through all x positions
            for(int z = -radius; z < radius; z++) { //Loop through z
                Block b = playerWorld.getHighestBlockAt(x + px, z + pz); //Get the first block at player position +x/+z
                if(b.getType() == Material.getMaterial(2)) { //Make sure the base block is grass
                    if(b.getLocation().getBlockY() != 256) { //Avoiding null pointers
                        b = playerWorld.getBlockAt(b.getLocation().add(0,1,0)); //Get block above grass
                        if(b.getType() == Material.AIR) { //Avoid overwriting blocks
                            ArrayList<Integer> list = getRandomBlock(x+px, z+pz, chance, flowerChance); //Generate random grass/flower based on chacne given by player
                            b.setTypeId(list.get(0)); //Update block values
                            b.setData(list.get(1).byteValue());
                        }
                    }
                } else {
                    for(int y = b.getLocation().getBlockY(); y > 0; y--) { //Go through all y values until a new block is found (for going under trees/underground caves)
                        b = playerWorld.getBlockAt(x + px, y, z + pz);
                        if(b.getType() == Material.GRASS) { //Check if base is grass
                            if(y != 256) //Safe and sorry
                                b = playerWorld.getBlockAt(b.getLocation().add(0,1,0));
                            if(b.getType() == Material.AIR) {
                                ArrayList<Integer> list = getRandomBlock(x+px, z+pz, chance, flowerChance); //Get random block based on user percentages if given
                                b.setTypeId(list.get(0)); //update block
                                b.setData(list.get(1).byteValue());
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public ArrayList<Integer> getRandomBlock(int x, int z, double chance, double flowerChance) {
        ArrayList<Integer> id = new ArrayList<>(); //To be returned
        id.add(0);
        id.add(0);
        
        double noise = Math.random(); //Get random from 0 to 1
        
        
        if(noise < chance) { //Chance of block being created = chance
            noise = (noise/chance); //Ensure a number between 0 and 1
            
            if (noise > 1-(flowerChance)) { //find case for which this is true
                id.set(0, 38);
                id.set(1, 1);
            }
            
            else if (noise > 1-(2*flowerChance)) {
                id.set(0, 38);
                id.set(1, 7);
            } 
            
            else if (noise > 1-(3*flowerChance)) {
                id.set(0, 38);
                id.set(1, 6);
            } 
            
            else if (noise > 1-(4*flowerChance)) {
                id.set(0, 38);
                id.set(1, 5);
            } 
            
            else if (noise > 1-(5*flowerChance)) {
                id.set(0, 38);
                id.set(1, 4);
            } 
            
            else if (noise > 1-(6*flowerChance)) {
                id.set(0, 38);
                id.set(1, 3);
            } 
            
            else if (noise > 1-(7*flowerChance)) {
                id.set(0, 38);
                id.set(1, 2);
            } 
            
            else if(noise > 1-(12*flowerChance)) {
                id.set(0, 38);
                id.set(1, 8);
            } 
            
            else if(noise > 1-(17*flowerChance)) {
                id.set(0, 38);
            }
            
            else if(noise > 1-(22*flowerChance)) {
                id.set(0, 37);
            }
            
            else {
                id.set(0, 31);
                id.set(1, 1);
            } 
        }
        
        return id; //Return block set
    }
    
    public void clearFlowers(int radius, Player player) {
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ(); //Player Locations

        World playerWorld = player.getWorld(); //Player's current world
        
        for(int x = -radius; x < radius; x++) { //Loop through all x values
            for(int z = -radius; z < radius; z++) { //Loop through all z values
                Block b = playerWorld.getHighestBlockAt(x + px, z + pz); //Get highest block
                if(isFlower(b.getType())) {
                    b.setType(Material.AIR); //If flower then remove (set air)
                } else {
                    for(int y = b.getLocation().getBlockY(); y > 0; y--) { //Loop through all blocks downward until flower is found
                        b = playerWorld.getBlockAt(x + px, y, z + pz);
                        if(isFlower(b.getType())) {
                            b.setType(Material.AIR); //Set to air when flower is found
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public boolean isFlower(Material m) { //Return true if BlockID matches any known flower ID
        int id = m.getId();
        if(id == 37) return true;
        if(id == 38) return true;
        if(id == 31) return true;
        
        return false;
    }
    
    public void snowGenerate(int radius, Player player) {
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ(); //Player Locations

        World playerWorld = player.getWorld(); //Current player world
        
        for(int x = -radius; x < radius; x++) { //Loop through x 
            for(int z = -radius; z < radius; z++) { //I think you get the picture
                Block b = playerWorld.getHighestBlockAt(x+px, z+pz).getRelative(BlockFace.DOWN); //Get highest solid block
                if(!b.getType().isTransparent() && !((CraftBlock)b).isLiquid()) { //Ensure that snow will be able to live on the block (Solid/Not Liquid)
                    if(b.getLocation().getBlockY() != 256) { //Ensure no null pointers
                        b = b.getRelative(BlockFace.UP); //Get air above solid block
                        if(b.getType() == Material.AIR) {
                            
                            b.setType(Material.SNOW); //Set air to snow
                            if(Math.random() > 0.9) { //Doubles height of snow for variety
                                b.setData((byte)1);
                            }
                            
                        }
                    }
                }
            }
        }
    }
    
    public void snowRemove(int radius, Player player) {
        int px = player.getLocation().getBlockX();
        int py = player.getLocation().getBlockY();
        int pz = player.getLocation().getBlockZ(); //Not explaining this again

        World playerWorld = player.getWorld(); //             ^^^
        
        for(int x = -radius; x < radius; x++) {
            for(int z = -radius; z < radius; z++) {
                Block b = playerWorld.getHighestBlockAt(x+px, z+pz); //Get highest block
                if(b.getType() == Material.SNOW) {
                    b.setType(Material.AIR); //Set to air if snow
                }
            }
        }
    }
    
}
