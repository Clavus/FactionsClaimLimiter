package clavus.factionsclaimlimiter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.LandClaimEvent;

public class FactionsClaimLimiterListener implements Listener
{
	private FactionsClaimLimiter plugin;
	
	public FactionsClaimLimiterListener(FactionsClaimLimiter plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLandClaimEvent(LandClaimEvent e)
	{
		if (e.isCancelled()) { return; }
		
		Faction f = e.getFaction();
		Player pl = e.getPlayer();
		if (pl != null && f != null && !plugin.canClaimLand(pl, f)) {
			e.setCancelled(true);
		}
	}
	
}
