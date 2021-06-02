package com.ssomar.score.commands.runnable.entity.commands;

import java.util.List;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.entity.EntityCommandTemplate;

/* PARTICLE {type} {quantity} {offset} {speed} */
public class ParticleCommand extends EntityCommandTemplate{

	@Override
	public void run(Player p, Entity entity, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		try {
			entity.getWorld().spawnParticle(Particle.valueOf(args.get(0)),
					entity.getLocation(),
					Integer.valueOf(args.get(1)),
					Double.valueOf(args.get(2)),
					Double.valueOf(args.get(2)) ,
					Double.valueOf(args.get(2)) ,
					Double.valueOf(args.get(3)), null);
		}catch(Exception e) {}
	}

	@Override
	public String verify(List<String> args) {
		return "";
	}

}
