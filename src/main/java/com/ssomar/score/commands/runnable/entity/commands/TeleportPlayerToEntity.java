package com.ssomar.score.commands.runnable.entity.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.entity.EntityCommandTemplate;

/* TELEPORT PLAYER TO ENTITY */
public class TeleportPlayerToEntity extends EntityCommandTemplate{

	@Override
	public void run(Player p, Entity entity, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		Location eLoc = entity.getLocation();
		
		if(!entity.isDead() && p.isOnline() && !p.isDead()) p.teleport(new Location(entity.getWorld(), eLoc.getX(), eLoc.getY(), eLoc.getZ()));
	}

	@Override
	public String verify(List<String> args) {
		return "";
	}

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("TELEPORT PLAYER TO ENTITY");
		return names;
	}

	@Override
	public String getTemplate() {
		return "TELEPORT PLAYER TO ENTITY";
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getExtraColor() {
		// TODO Auto-generated method stub
		return null;
	}
}
