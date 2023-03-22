package dev.cammiescorner.guts_guns_glory.common.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class GGGConfig extends MidnightConfig {
	@Entry public static boolean losingBloodCausesUnconsciousness = true;
	@Entry public static boolean headshotsBoostDamage = false;
	@Entry public static boolean meleeCanHeadshot = false;
	@Entry public static float headshotDamageMultiplier = 2F;
}
