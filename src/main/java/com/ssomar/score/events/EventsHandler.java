package com.ssomar.score.events;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.SecurityOPCommands;
import com.ssomar.score.menu.InteractionGUI;
import com.ssomar.score.nofalldamage.NoFallDamageEvt;

public class EventsHandler {
	
	private static EventsHandler instance;
	
	private SCore main;
	
	public void setup(SCore main) {
		this.main=main;
		setupEvents();
	}
	
	public void setupEvents() {
		main.getServer().getPluginManager().registerEvents(new SecurityOPCommands(), main);
		
		main.getServer().getPluginManager().registerEvents(new NoFallDamageEvt(), main);
		
		main.getServer().getPluginManager().registerEvents(new PlayerReconnexion(), main);
		
		main.getServer().getPluginManager().registerEvents(new InteractionGUI(), main);
	}

	public static EventsHandler getInstance() {
	    if (instance == null) instance = new EventsHandler();
	    return instance;
	 }
}