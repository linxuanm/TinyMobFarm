package cn.davidma.tinymobfarm.common;

import cn.davidma.tinymobfarm.common.item.Lasso;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod(value = Reference.MOD_ID)
public class TinyMobFarm {

	public static TinyMobFarm instance;
	
	@ObjectHolder(Reference.MOD_ID + ":lasso")
	public static Item lasso;
	
	public TinyMobFarm() {
		instance = this;
		
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(lasso = new Lasso(new Item.Properties()).setRegistryName(Reference.getLocation("lasso")));
	}
}
