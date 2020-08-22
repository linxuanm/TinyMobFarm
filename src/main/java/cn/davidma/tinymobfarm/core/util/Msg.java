package cn.davidma.tinymobfarm.core.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class Msg {

	public static void tellPlayer(PlayerEntity player, String text) {
		player.sendMessage(new StringTextComponent(I18n.format(text)), Util.field_240973_b_);
	}
	
	public static StringTextComponent tooltip(String text, Object... parameters) {
		StringTextComponent msg = new StringTextComponent(I18n.format(text, parameters));
		msg.func_230530_a_(Style.field_240709_b_.func_240718_a_(Color.func_240744_a_(TextFormatting.GRAY)));
		return msg;
	}
}
