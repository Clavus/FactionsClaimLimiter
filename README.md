FactionsClaimLimiter
================

Bukkit plugin for Minecraft that changes the rules for the Factions plugin a bit. If your faction has too few members, you can claim less territory than normal. This is to prevent small groups and solo players from littering the map with claimed terrain.

Configuration
---------
See the config.yml (auto-created in the plugins/FactionsClaimLimiter folder when starting the server)

 * <b>message: "my message"</b> // The message to display if one of the rules is met
 * <b>rules:</b>
 * -  <b>players_in_faction: 2</b> // Number of players you need in your faction for this rule to trigger
 * -  <b>max_claims: 4</b> // Max amount of claimed chunks you're allowed to have with the above number of players
