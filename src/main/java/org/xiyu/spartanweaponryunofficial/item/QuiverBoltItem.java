package org.xiyu.spartanweaponryunofficial.item;

import java.util.List;
import java.util.Optional;

import org.xiyu.spartanweaponryunofficial.ModSpartanWeaponry;
import org.xiyu.spartanweaponryunofficial.api.tags.ModItemTags;
import org.xiyu.spartanweaponryunofficial.inventory.QuiverBoltMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;

public class QuiverBoltItem extends QuiverBaseItem 
{
	public static final ResourceLocation TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_small.png");
	public static final ResourceLocation TEXTURE_MEDIUM = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_medium.png");
	public static final ResourceLocation TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_large.png");
	public static final ResourceLocation TEXTURE_HUGE = ResourceLocation.fromNamespaceAndPath(ModSpartanWeaponry.ID, "textures/model/quiver_bolt_huge.png");

	public QuiverBoltItem(int inventorySize) 
	{
		super(inventorySize);
	}

	@Override
	public void openGui(ItemStack stack, Player player, SlotType slotType, int slot) 
	{
		if(!(player instanceof ServerPlayer serverPlayer))
			return;

		((IPlayerExtension) serverPlayer).openMenu(new ContainerProvider(Component.translatable("gui." + ModSpartanWeaponry.ID + ".quiver_bolt.title"), stack), buf ->
		{
			buf.writeEnum(slotType);
			buf.writeInt(slot);
		});
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) 
	{
		tooltip.add(Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.ammo.type", 
				Component.translatable("tooltip." + ModSpartanWeaponry.ID + ".modifiers.ammo.bolt").withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_AQUA));
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	protected class ContainerProvider implements MenuProvider
	{
		private final Component displayName;
		private final ItemStack quiverStack;
		
		protected ContainerProvider(Component displayName, ItemStack quiverStack)
		{
			this.displayName = displayName;
			this.quiverStack = quiverStack;
		}

		@Override
		public AbstractContainerMenu createMenu(int id, Inventory inventory,
				Player player)
		{
			return new QuiverBoltMenu(id, inventory, quiverStack);
		}

		@Override
		public Component getDisplayName() 
		{
			return displayName;
		}
	}

	@Override
	public boolean isAmmoValid(ItemStack pickedUpStack, ItemStack quiver) 
	{
		return pickedUpStack.is(ModItemTags.BOLTS);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stackIn) 
	{
		return makeTooltipImage(stackIn, true);
	}

}
