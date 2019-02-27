package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.client.gui.HandlerGui;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.IProxy;
import cn.davidma.tinymobfarm.core.Reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION)
public class TinyMobFarm {
	
	@Instance
	public static TinyMobFarm instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.SERVER_PROXY)
	public static IProxy proxy;
	
	public static CreativeTabs creativeTabTinyMobFarm = new CreativeTabTinyMobFarm();
	
	public static Item lasso;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityMobFarm.class, new ResourceLocation(Reference.MOD_ID + ":mob_farm_tile_entity"));
		NetworkRegistry.INSTANCE.registerGuiHandler(TinyMobFarm.instance, new HandlerGui());
		proxy.init();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
