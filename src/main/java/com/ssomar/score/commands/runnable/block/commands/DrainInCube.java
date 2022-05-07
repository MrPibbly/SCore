package com.ssomar.score.commands.runnable.block.commands;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.block.BlockCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DrainInCube extends BlockCommand {

    @Override
    public void run(Player p, @NotNull Block block, Material oldMaterial, List<String> args, ActionInfo aInfo) {


        try {
            int radius = Integer.parseInt(args.get(0));

            List<Material> whiteList = new ArrayList<>();
            if(args.size() <= 1) {
                whiteList.add(Material.WATER);
                whiteList.add(Material.LAVA);
            }
            else if (args.get(1).toUpperCase().equals("LAVA")){
                whiteList.add(Material.LAVA);
            }
            else if (args.get(1).toUpperCase().equals("WATER")){
                whiteList.add(Material.WATER);
            }

            if (radius < 10) {
                for (int y = -radius; y < radius + 1; y++) {
                    for (int x = -radius; x < radius + 1; x++) {
                        for (int z = -radius; z < radius + 1; z++) {

                            Block toBreak = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

                            if (whiteList.contains(toBreak.getType())) {
                                toBreak.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public String verify(List<String> args) {
        return "";
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("DRAININCUBE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "DRAININCUBE {radius} [LAVA or WATER (no need if you want both)]";
    }

    @Override
    public ChatColor getColor() {
        return null;
    }

    @Override
    public ChatColor getExtraColor() {
        return null;
    }

}
