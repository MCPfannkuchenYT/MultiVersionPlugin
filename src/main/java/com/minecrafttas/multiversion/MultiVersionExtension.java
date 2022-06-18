package com.minecrafttas.multiversion;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

/**
 * Main Multi Version Configuration Extension.
 * Holds mod information.
 * @author Pancake
 */
public abstract class MultiVersionExtension {

	/**
	 * Id of the mod
	 * @return Mod Id
	 */
	public abstract Property<String> getId();
	
	/**
	 * Version of the mod
	 * @return Mod Version
	 */
	public abstract Property<String> getVersion();
	
	/**
	 * Name of the mod
	 * @return Mod Name
	 */
	public abstract Property<String> getName();
	
	/**
	 * Group of the mod
	 * @return Mod Group
	 */
	public abstract Property<String> getGroup();
	
	/**
	 * Description of the mod
	 * @return Mod Description
	 */
	public abstract Property<String> getDescription();
	
	/**
	 * Source of the mod
	 * @return Mod Source Code
	 */
	public abstract Property<String> getSource();
	
	/**
	 * Website of the mod
	 * @return Mod Website
	 */
	public abstract Property<String> getWebsite();
	
	/**
	 * Minecraft Versions this mod supports
	 * @return Minecraft Version
	 */
	public abstract ListProperty<String> getMcversions();
	
	/**
	 * Current Minecraft Version
	 * @return Minecraft Version
	 */
	public abstract Property<String> getMcversion();
	
	/**
	 * Authors of the mod
	 * @return Authors
	 */
	public abstract ListProperty<String> getAuthors();
	
}
