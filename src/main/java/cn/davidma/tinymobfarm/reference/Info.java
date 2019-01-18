package cn.davidma.tinymobfarm.reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Info {
	public static final String MOD_ID = "tinymobfarm";
	public static final String NAME = "Tiny Mob Farm";
	public static final String VERSION = "1.0.0";
	public static final String MC_VERSIONS = "[1.12.2]";
	public static final String CLIENT_PROXY = "cn.davidma.tinymobfarm.proxy.ClientProxy";
	public static final String COMMON_PROXY = "cn.davidma.tinymobfarm.proxy.CommonProxy";
	
	public static final int FARM_GUI = 1;
	
	private static final int[][] STATS = {
		{1, 2, 3},
		{1, 2, 2},
		{1, 2},
		{1, 1, 2},
		{1},
		{0, 1, 1},
		{0, 0, 1},
		{0}
	};
	
	public static final int DURABILITY_COST_FROM_FARM_ID(int id, Random rand) {
		
		// Oh wow that was a long name.
		return choice(STATS[id], rand);
	}
	
	public static final List<String> TOOLTIP_FROM_FARM_ID(int id) {
		
		// Terrible formatting but meh.
		String[] setup = {
			"Generates loot every " + TinyMobFarmConfig.GENERATOR_SPEED[id] + " seconds.",
			"Items are ejected to ajacent containers.",
			"Lasso durability loss per generation cycle:",
		};
		
		// To circumvent abstract list (OCD).
		List<String> tooltip = new ArrayList<String>();
		
		for (String i: setup) tooltip.add(i);
		
		Map<Integer, Integer> chances = getChances(STATS[id]);
		for (int key: chances.keySet()) {
			tooltip.add(getTip(chances.get(key), key));
		}
		
		return tooltip;
	}
	
	private static Map<Integer, Integer> getChances(int[] choices) {
		Map<Integer, Integer> out = new HashMap<Integer, Integer>();
		for (int i: choices) {
			if (!out.containsKey(i)) out.put(i, 0);
			out.put(i, out.get(i) + 1);
		}
		for (int key: out.keySet()) {
			out.put(key, (int) (((double) out.get(key) / (double) choices.length) * 100));
		}
		return out;
	}
	
	private static int choice(int[] choices, Random rand) {
		return choices[rand.nextInt(choices.length)];
	}
	
	private static String getTip(int chance, int durability) {
		if (durability == 0) return String.format("- %d%% chance to loss no durability", chance);
		return String.format("- %d%% chance to lose %d durability", chance, durability);
	}
}
