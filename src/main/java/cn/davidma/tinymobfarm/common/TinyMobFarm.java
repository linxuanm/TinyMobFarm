package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.client.gui.HandlerGui;
import cn.davidma.tinymobfarm.common.tileentity.MobFarmTileEntity;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MOD_ID, name=Reference.NAME, version=Reference.VERSION)
public class TinyMobFarm {
	
	@Instance
	public static TinyMobFarm instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.COMMON_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(MobFarmTileEntity.class, Reference.MOD_ID + "_GeneratorTileEntity");
		NetworkRegistry.INSTANCE.registerGuiHandler(TinyMobFarm.instance, new HandlerGui());
		proxy.registerTESR();
	}
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event) {

	}
}
