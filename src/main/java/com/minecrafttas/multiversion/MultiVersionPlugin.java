package com.minecrafttas.multiversion;

import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.api.mappings.layered.spec.LayeredMappingSpecBuilder;
import net.fabricmc.loom.bootstrap.LoomGradlePluginBootstrap;
import net.fabricmc.loom.configuration.providers.mappings.GradleMappingContext;
import net.fabricmc.loom.configuration.providers.mappings.LayeredMappingSpec;
import net.fabricmc.loom.configuration.providers.mappings.LayeredMappingSpecBuilderImpl;
import net.fabricmc.loom.configuration.providers.mappings.LayeredMappingsDependency;

/**
 * Main multi version plugin.
 * Applies the project to the gradle workspace.
 * @author Pancake
 */
public class MultiVersionPlugin implements Plugin<Project> {

	/**
	 * User configuration extension for multi version mod properties
	 */
	private MultiVersionExtension config;

	/**
	 * Applies the plugin
	 * Prepares:
	 *  - Plugins
	 *  - Main Extension
	 */
	@Override
	public void apply(Project project) {
		// Apply Java Plugin
		project.getPluginManager().apply("java-library");
		// Add Java Compatibility
		project.getExtensions().getByType(JavaPluginExtension.class).setSourceCompatibility(JavaVersion.VERSION_17);
		project.getExtensions().getByType(JavaPluginExtension.class).setTargetCompatibility(JavaVersion.VERSION_17);
		// Add Extension
		project.getExtensions().add("mod", MultiVersionExtension.class);
		/* Do a lot of stuff after evaluation */
		project.afterEvaluate(_p -> {
			// Make sure config is full
			config = project.getExtensions().getByType(MultiVersionExtension.class);
			if (!config.getAuthors().isPresent()) System.err.println("Authors Property missing!");
			if (!config.getDescription().isPresent()) System.err.println("Description Property missing!");
			if (!config.getGroup().isPresent()) System.err.println("Group Property missing!");
			if (!config.getId().isPresent()) System.err.println("Id Property missing!");
			if (!config.getMcversions().isPresent()) System.err.println("MCVersions Property missing!");
			if (!config.getMcversion().isPresent()) System.err.println("MCVersion Property missing!");
			if (!config.getName().isPresent()) System.err.println("Name Property missing!");
			if (!config.getVersion().isPresent()) System.err.println("Version Property missing!");
			if (!config.getSource().isPresent()) config.getSource().set("");
			if (!config.getWebsite().isPresent()) config.getWebsite().set("");
			
			// Add loom plugin
			this.loom(project, config.getMcversion().get());
		});
	}
	
	/**
	 * Applies the loom plugin
	 * @param project Project
	 */
	public void loom(Project project, String mc) {
		// Apply Loom Plugin
		project.getPlugins().apply(LoomGradlePluginBootstrap.class);
		// Update intermediary url if necessary
		project.getExtensions().findByType(LoomGradleExtensionAPI.class).getIntermediaryUrl().set("https://maven.legacyfabric.net/net/fabricmc/intermediary/%1$s/intermediary-%1$s-v2.jar");
		// Add dependencies
		project.getDependencies().add("minecraft", "com.mojang:minecraft:" + mc);
		LayeredMappingSpecBuilderImpl builder = new LayeredMappingSpecBuilderImpl();
		LayeredMappingSpec builtSpec = builder.build();
		Action<LayeredMappingSpecBuilder> action = LayeredMappingSpecBuilder::officialMojangMappings;
		action.execute(builder);
		project.getDependencies().add("mappings", new LayeredMappingsDependency(project, new GradleMappingContext(project, builtSpec.getVersion().replace("+", "_").replace(".", "_")), builtSpec, builtSpec.getVersion()));
	}
	
}
