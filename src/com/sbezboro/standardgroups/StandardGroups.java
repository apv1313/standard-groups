package com.sbezboro.standardgroups;

import com.sbezboro.standardgroups.commands.GroupsCommand;
import com.sbezboro.standardgroups.listeners.*;
import com.sbezboro.standardgroups.managers.GroupManager;
import com.sbezboro.standardgroups.managers.MapManager;
import com.sbezboro.standardgroups.persistence.storages.GroupStorage;
import com.sbezboro.standardplugin.StandardPlugin;
import com.sbezboro.standardplugin.SubPlugin;
import com.sbezboro.standardplugin.commands.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


public class StandardGroups extends JavaPlugin implements SubPlugin {
	private static StandardGroups instance;
	
	private StandardPlugin basePlugin;

	private StandardConfig config;
	
	private GroupStorage groupStorage;
	private GroupManager groupManager;

	private MapManager mapManager;
	
	public StandardGroups() {
		instance = this;
	}

	public static StandardGroups getPlugin() {
		return instance;
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		getConfig().options().copyDefaults(true);
		
		basePlugin = StandardPlugin.getPlugin();
		basePlugin.registerSubPlugin(this);
		
		config = new StandardConfig(basePlugin);
		
		groupStorage = new GroupStorage(basePlugin);
		groupManager = new GroupManager(basePlugin, this, groupStorage);

		mapManager = new MapManager(basePlugin, this);

		reloadPlugin();

		registerEvents();
	}

	@Override
	public void onDisable() {
		super.onDisable();

		Bukkit.getScheduler().cancelTasks(this);

		mapManager.unload();
	}

	@Override
	public void reloadPlugin() {
		config.reload();
		
		reloadConfig();
	}

	@Override
	public List<ICommand> getCommands() {
		List<ICommand> commands = new ArrayList<ICommand>();
		commands.add(new GroupsCommand(basePlugin, this));
		return commands;
	}

	private void registerEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new BlockBreakListener(basePlugin, this), this);
		pluginManager.registerEvents(new BlockPistonExtendListener(basePlugin, this), this);
		pluginManager.registerEvents(new BlockPistonRetractListener(basePlugin, this), this);
		pluginManager.registerEvents(new BlockPlaceListener(basePlugin, this), this);
		pluginManager.registerEvents(new CreatureSpawnListener(basePlugin, this), this);
		pluginManager.registerEvents(new EntityChangeBlockListener(basePlugin, this), this);
		pluginManager.registerEvents(new EntityExplodeListener(basePlugin, this), this);
		pluginManager.registerEvents(new EntityTargetListener(basePlugin, this), this);
		pluginManager.registerEvents(new HangingBreakListener(basePlugin, this), this);
		pluginManager.registerEvents(new HangingPlaceListener(basePlugin, this), this);
		pluginManager.registerEvents(new InventoryMoveListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerChatListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerBucketEmptyListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerBucketFillListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerInteractListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerDamageListener(basePlugin, this), this);
		pluginManager.registerEvents(new PlayerMoveListener(basePlugin, this), this);
	}
	
	public int getGroupNameMinLength() {
		return config.getGroupNameMinLength();
	}
	
	public int getGroupNameMaxLength() {
		return config.getGroupNameMaxLength();
	}

	public int getGroupLandGrowth() {
		return config.getGroupLandGrowth();
	}

	public int getGroupLandGrowthDays() {
		return config.getGroupLandGrowthDays();
	}

	public int getGroupAutoKickDays() {
		return config.getGroupAutoKickDays();
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	@Override
	public String getSubPluginName() {
		return "StandardGroups";
	}
}
