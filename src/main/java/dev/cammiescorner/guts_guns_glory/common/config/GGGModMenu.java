package dev.cammiescorner.guts_guns_glory.common.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.cammiescorner.guts_guns_glory.GutsGunsGlory;

public class GGGModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> GGGConfig.getScreen(parent, GutsGunsGlory.MOD_ID);
	}
}
