package clavus.factionsclaimlimiter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.FactionsEventChunkChange;

public class FactionsClaimLimiterListener implements Listener
{
	private FactionsClaimLimiter plugin;
	
	public FactionsClaimLimiterListener(FactionsClaimLimiter plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onLandClaimEvent(FactionsEventChunkChange e)
	{
		if (e.isCancelled()) { return; }
		
		Faction f = e.getNewFaction();
		Player pl = e.getUSender().getPlayer();
		if (pl != null && f != null && !plugin.canClaimLand(pl, f)) {
			e.setCancelled(true);
		}
	}
	
}
