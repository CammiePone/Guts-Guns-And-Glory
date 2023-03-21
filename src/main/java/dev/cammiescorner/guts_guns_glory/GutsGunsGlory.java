package dev.cammiescorner.guts_guns_glory;

import dev.cammiescorner.guts_guns_glory.common.config.GGGConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GutsGunsGlory implements ModInitializer {
	public static final String MOD_ID = "gunsgutsglory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize(ModContainer mod) {
		MidnightConfig.init(MOD_ID, GGGConfig.class);
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
