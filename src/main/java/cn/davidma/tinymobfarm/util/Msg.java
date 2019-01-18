package cn.davidma.tinymobfarm.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Msg {

	public static void tellPlayer(EntityPlayer player, String msg) {
		player.sendMessage(new TextComponentString(msg));
	}
}
