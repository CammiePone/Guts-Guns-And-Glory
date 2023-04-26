package dev.cammiescorner.guts_guns_glory.common.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class GGGConfig extends MidnightConfig {
	@Entry public static boolean losingBloodCausesUnconsciousness = true;
	@Entry public static boolean vitalsHitBoostsDamage = false;
	@Entry public static boolean meleeCanHitVitals = false;
	@Entry public static boolean toolsCanCauseBleeding = true;
	@Entry public static float vitalsHitDamageMultiplier = 2F;
}
