package cn.davidma.tinymobfarm.core.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Msg {

	public static void tellPlayer(EntityPlayer player, String text) {
		player.sendMessage(new TextComponentString(I18n.format(text)));
	}
	
	public static TextComponentString tooltip(String text, Object... parameters) {
		TextComponentString msg = new TextComponentString(I18n.format(text, parameters));
		msg.applyTextStyle(TextFormatting.GRAY);
		return msg;
	}
}
