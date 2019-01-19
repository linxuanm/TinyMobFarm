package cn.davidma.tinymobfarm.util;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class FakePlayerHelper {
	
	private static GameProfile fakeProfile = new GameProfile(UUID.randomUUID(), "[TinyMobFarmFakePlayer]");
	private static FakePlayer fakePlayer= null;

	public static FakePlayer getPlayer(WorldServer world) {
		if (fakePlayer == null) fakePlayer = new FakePlayer(world, fakeProfile);
		return fakePlayer;
	}
}
