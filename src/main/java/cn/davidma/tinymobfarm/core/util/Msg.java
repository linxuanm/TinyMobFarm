package cn.davidma.tinymobfarm.core.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class Msg {

	public static void tellPlayer(PlayerEntity player, String text) {
		player.sendMessage(new StringTextComponent(I18n.get(text)), Util.NIL_UUID);
	}
	
	public static StringTextComponent tooltip(String text, Object... parameters) {
		StringTextComponent msg = new StringTextComponent(I18n.get(text, parameters));
		msg.setStyle(Style.EMPTY.applyFormat(TextFormatting.GRAY));
		return msg;
	}
}
