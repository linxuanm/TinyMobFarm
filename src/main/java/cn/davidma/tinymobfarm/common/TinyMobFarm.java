package cn.davidma.tinymobfarm.common;

import java.util.ArrayList;
import java.util.List;

import cn.davidma.tinymobfarm.client.gui.GuiMobFarm;
import cn.davidma.tinymobfarm.common.block.BlockMobFarm;
import cn.davidma.tinymobfarm.common.item.ItemLasso;
import cn.davidma.tinymobfarm.common.tileentity.TileEntityMobFarm;
import cn.davidma.tinymobfarm.core.EnumMobFarm;
import cn.davidma.tinymobfarm.core.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLPlayMessages.OpenContainer;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(Reference.MOD_ID)
public class TinyMobFarm {

	public static TinyMobFarm instance;
	
	public static ItemGroup creativeTab;
	
	public static Item lasso;
	public static List<Block> mobFarms;
	public static TileEntityType<TileEntityMobFarm> tileEntityMobFarm;
	
	public TinyMobFarm() {
		instance = this;
		creativeTab = new ItemGroup("tiny_mob_farm") {
			@Override
			public ItemStack createIcon() {
				return new ItemStack(TinyMobFarm.mobFarms.get(0));
			}
		};
		
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> {
			return (openContainer) -> {
				ResourceLocation location = openContainer.getId();
				if (location.toString().equals(Reference.MOD_ID + ":mob_farm_gui")) {
					EntityPlayerSP player = Minecraft.getInstance().player;
					BlockPos pos = openContainer.getAdditionalData().readBlockPos();
					TileEntity tileEntity = player.world.getTileEntity(pos);
					if (tileEntity instanceof TileEntityMobFarm) {
						return new GuiMobFarm(player.inventory, (TileEntityMobFarm) tileEntity);
					}
				}
				return null;
			};
		});
		
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::registerBlocks);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::registerItems);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		mobFarms = new ArrayList<Block>();
		
		for (EnumMobFarm i: EnumMobFarm.values()) {
			Block mobFarm = new BlockMobFarm(i).setRegistryName(Reference.getLocation(i.getRegistryName()));
			mobFarms.add(mobFarm);
			registry.register(mobFarm);
		}
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(lasso = new ItemLasso(new Item.Properties()).setRegistryName(Reference.getLocation("lasso")));
		
		for (Block i: mobFarms) {
			Item itemBlockMobFarm = new ItemBlock(i, new Item.Properties().group(creativeTab)).setRegistryName(i.getRegistryName());
			registry.register(itemBlockMobFarm);
		}
	}
	
	@SubscribeEvent
	public void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
		tileEntityMobFarm = TileEntityType.register(Reference.MOD_ID + ":mob_farm_tile_entity", TileEntityType.Builder.create(TileEntityMobFarm::new));
	}
}
