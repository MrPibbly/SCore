package com.ssomar.score.commands.runnable.entity.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.entity.EntityCommandTemplate;

/* KILL */
public class Kill extends EntityCommandTemplate{

	@Override
	public void run(Player p, Entity entity, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		if(!entity.isDead()) entity.remove();
	}

	@Override
	public String verify(List<String> args) {
		return "";
	}
	
	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("KILL");
		return names;
	}

	@Override
	public String getTemplate() {
		return "KILL";
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
