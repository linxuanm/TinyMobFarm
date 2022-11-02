package cn.davidma.tinymobfarm.core.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerHelper {

	public static final String FAKE_NAME = "[TinyMobFarm_DanielTheEgg]";
	
	private static final GameProfile GAME_PROFILE = new GameProfile(UUID.randomUUID(), FAKE_NAME);
	private static FakePlayer fakePlayer;
	
	public static FakePlayer getPlayer(ServerWorld world) {
		if (fakePlayer == null) fakePlayer = new FakePlayer(world, GAME_PROFILE);
		return fakePlayer;
	}
}
