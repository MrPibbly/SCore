package com.ssomar.score.commands.runnable.block.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.block.BlockCommandTemplate;
import com.ssomar.score.commands.runnable.player.PlayerCommandsExecutor;

/* AROUND {distance} {true or false} {Your commands here} */
public class Around extends BlockCommandTemplate{
	@Override
	public String verify(List<String> args) {
		String error = "";

		String around = "AROUND {distance} {Your commands here}";
		
		if(args.size() < 2) error = notEnoughArgs+around;
		else if(args.size() > 2) { 
			try {
				Double.valueOf(args.get(0));
			}catch(NumberFormatException e){
				error = invalidDistance+args.get(0)+" for command: "+around;
			}
		}

		return error;
	}

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("AROUND");
		return names;
	}

	@Override
	public String getTemplate() {
		return "AROUND {distance} {Your commands here}";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.LIGHT_PURPLE;
	}

	@Override
	public ChatColor getExtraColor() {
		return ChatColor.DARK_PURPLE;
	}

	@Override
	public void run(Player p, Block block, Material oldMaterial, List<String> args, ActionInfo aInfo,
			boolean silenceOutput) {
		try {
			double distance = Double.valueOf(args.get(0));
			
			for (Entity e: block.getWorld().getNearbyEntities(block.getLocation(), distance, distance, distance)) {
				if(e instanceof Player) {
					Player target =  (Player) e;
					if(target.hasMetadata("NPC")) continue;
					
					/* regroup the last args that correspond to the commands */
					StringBuilder prepareCommands = new StringBuilder();
					for(String s : args.subList(1, args.size())) {
						prepareCommands.append(s);
						prepareCommands.append(" ");
					}
					prepareCommands.deleteCharAt(prepareCommands.length()-1);				

					String buildCommands = prepareCommands.toString();
					String [] tab;
					if(buildCommands.contains("+++")) tab = buildCommands.split("\\+\\+\\+");
					else {
						tab = new String[1];
						tab[0] = buildCommands;
					}
					for(String s : tab) {
						while(s.startsWith(" ")) {
							s = s.substring(1, s.length());
						}
						while(s.endsWith(" ")) {
							s = s.substring(0, s.length()-1);
						}
						if(s.startsWith("/")) s = s.substring(1, s.length());

						Location loc = target.getLocation();

						s = s.replaceAll("%target_x%", loc.getX()+"");
						s = s.replaceAll("%target_y%", loc.getY()+"");
						s = s.replaceAll("%target_z%", loc.getZ()+"");
						s = s.replaceAll("%target%", target.getName());
						s = s.replaceAll("%target_uuid%", target.getUniqueId()+"");
						new PlayerCommandsExecutor(Arrays.asList(s), p, silenceOutput, target, aInfo).runPlayerCommands(silenceOutput);		
					}				
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}