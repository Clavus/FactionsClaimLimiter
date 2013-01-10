package clavus.factionsclaimlimiter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;

public class FactionsClaimLimiter extends JavaPlugin
{
	private FactionsClaimLimiterListener listener = new FactionsClaimLimiterListener(this);
	private String chatTag = ChatColor.YELLOW + " ~ ";
	private String consoleTag = "[FactionsClaimLimiter] ";
	
	private String configMessage = "Your faction is too small to claim more land! Recruit more members first.";
	
	private P factions;
	
	private Logger log;
	
	private FileConfiguration config;
	private ArrayList<HashMap<String, Integer>> rules = new ArrayList<HashMap<String, Integer>>();
	
	public FactionsClaimLimiter()
	{
		log = Logger.getLogger("Minecraft");
	}
	
	public void onEnable() 
	{
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(listener, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		
		factions = (P) pm.getPlugin("Factions");
		if (factions == null) {
			scream("Failed to enable " + pdfFile.getName() + ", there is no Factions plugin!");
			pm.disablePlugin(this);
			return;
		}
		
		loadConfig();
		parseConfig();
		
		print(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public void onDisable() 
	{
		PluginDescriptionFile pdfFile = this.getDescription();
        print("Closing " + pdfFile.getName() + "!");
	}
	
	public boolean canClaimLand(Player pl)
	{
		FPlayer fpl = FPlayers.i.get(pl);
		Faction plFaction = fpl.getFaction();
		
		if (fpl.isAdminBypassing()) { return true; }
		
		int numMembers = plFaction.getFPlayers().size();
		int numClaimed = plFaction.getLandRounded();
		
		for (HashMap<String, Integer> rule : rules) {
			if (numMembers == rule.get("players_in_faction") && numClaimed + 1 > rule.get("max_claims")) {
				sendMessageTo(pl, configMessage);
				return false;
			}
		}
		
		return true;
	}
	
	////Config stuff ////
	
	private void loadConfig()
	{
		config = this.getConfig();
		if (config == null) { 
			this.saveDefaultConfig(); 
			config = this.getConfig();
		}
		
		config.options().copyDefaults(true);
		this.saveConfig();
	}
	
	@SuppressWarnings({ "unchecked" })
	private void parseConfig()
	{
		configMessage = config.getString("message");
		List<LinkedHashMap<String, Object>> configRules = (List<LinkedHashMap<String, Object>>) config.getList("rules");
		
		for(LinkedHashMap<String, Object> configRule : configRules) {
			HashMap<String, Integer> rule = new HashMap<String, Integer>();
			for(String key : configRule.keySet()) {
				int val = Integer.parseInt(configRule.get(key).toString());
				rule.put(key, val);
			}
			rules.add(rule);
		}
	}
	
	////Messaging and print stuff ////
	
	public void sendMessageTo(Player pl, String msg)
	{
		if (pl == null) {
			print(msg);
		}
		else {
			pl.sendMessage(chatTag + ChatColor.WHITE + msg);
		}
	}
	
	public void print(String msg)
	{
		log.info(consoleTag + msg);
	}
	
	public void scream(String msg)
	{
		log.log(Level.SEVERE, consoleTag + msg);
	}
	
	public void scream(String msg, Throwable thrown)
	{
		log.log(Level.SEVERE, consoleTag + msg, thrown);
	}
	
}
