package cn.davidma.tinymobfarm.core.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Msg {

	public static void tellPlayer(EntityPlayer player, String text) {
		player.sendMessage(new TextComponentString(I18n.format(text)));
	}
}
