package at.doebi;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class MCMEPVP extends JavaPlugin{
	//create a HashMap and a count for player teams
	public static HashMap<String, Integer> PlayerTeams = new HashMap<String, Integer>();
	public static int Blue = 0;
	public static int Red = 0;

	//create a HashMap for storing class loadouts
	public static HashMap<String, Inventory> Classes = new HashMap<String, Inventory>();

	//onEnable, run when server starts
	@Override
	public void onEnable() {
		//registering Listener
		getServer().getPluginManager().registerEvents(new MCMEPVPListener(this), this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("pvp")){
			//Identify the player
			String playername = sender.getName();
			Player player = sender.getServer().getPlayer(playername);
			//What to do when a player types /pvp
			String method = args[0];
			if(method == "join"){
				//Assign player to a team with fewest members
				//For lack of a better idea 1 means red team and 2 means blue
				if(Blue > Red){
					Red = Red + 1;
					String Playername = sender.getName();
					PlayerTeams.put(Playername, new Integer(1)); 
				}
				if(Blue < Red){
					Blue = Blue + 1;
					String Playername = sender.getName();
					PlayerTeams.put(Playername, new Integer(2)); 
				}
				else{
					@SuppressWarnings("unused")
					boolean random = (Math.random() < 0.5);
					if(random = true){
						Red = Red + 1;
						String Playername = sender.getName();
						PlayerTeams.put(Playername, new Integer(1));
					}
					else{
						Blue = Blue + 1;
						String Playername = sender.getName();
						PlayerTeams.put(Playername, new Integer(2)); 
					}
				}
			}
			if(method == "leave"){
				//Assign Player to spectator if they leave
				String Playername = sender.getName().toLowerCase();
				MCMEPVP.PlayerTeams.put(Playername, new Integer(0));
			}
			if(method == "class"){
				String classname = args[1];
				//Allow mods to create classes by storing their inventory in a hashmap
				//with the class as the key
				if(classname == "set"){
					String classtoset = args[2];
					PlayerInventory createinventory = player.getInventory();
					Classes.put(classtoset, createinventory);
				}
				else{
					//set a player's inventory to that of the given class if it exists
					Inventory classinventory = Classes.get(classname);
					if(classinventory == null){
						player.sendMessage("no such class");
					}
					else{
						player.getInventory().clear();
						ItemStack[] contents = classinventory.getContents();
						player.getInventory().setContents(contents);
					}
				}
			}

			return true;
		}
		return false; 
	}
}
