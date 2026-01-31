package org.xiyu.spartanweaponryunofficial.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.xiyu.spartanweaponryunofficial.api.IWeaponTraitContainer;
import org.xiyu.spartanweaponryunofficial.api.WeaponTraits;
import org.xiyu.spartanweaponryunofficial.util.ItemStackDataHelper;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class DecapitateLootModifier extends LootModifier 
{
	public static final MapCodec<DecapitateLootModifier> DECAPITATE_CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance).and(
				instance.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("skull").forGetter(modifier -> modifier.skull)).t1()
			).apply(instance, DecapitateLootModifier::new));
	
	private Item skull;

	public DecapitateLootModifier(LootItemCondition[] conditionsIn, Item skullItem)
	{
		super(conditionsIn);
		skull = skullItem;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		Entity killer = context.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
		if(killer == null)
			return generatedLoot;
		if(killer instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity)killer;
			ItemStack weapon = living.getMainHandItem();
//			ItemStack weapon = context.get(LootContextParams.TOOL);
			
			// TODO: See if there is a way that this weapon (above) can be retrieved in a dual-wielding friendly way.
			if(context.getRandom().nextDouble() < WeaponTraits.DECAPITATE.get().getMagnitude() / 100.0f && weapon.getItem() instanceof IWeaponTraitContainer)
			{
				IWeaponTraitContainer<?> container = (IWeaponTraitContainer<?>)weapon.getItem();
				
				if(container.hasWeaponTraitWithType(WeaponTraits.TYPE_DECAPITATE))
				{
					ItemStack skullStack = new ItemStack(skull);
					Entity thisEntity = context.getParam(LootContextParams.THIS_ENTITY);
					if(thisEntity instanceof Player)
					{
						Player player = (Player)thisEntity;
						// Add the player NBT data to the skull ItemStack
						ItemStackDataHelper.updateTag(skullStack, tag -> tag.putString("SkullOwner", player.getGameProfile().getName()));
					}
					generatedLoot.add(skullStack);
				}
			}
		}
		return generatedLoot;
	}

/*	public static class Serializer extends GlobalLootModifierSerializer<DecapitateLootModifier>
	{
		
		@Override
		public DecapitateLootModifier read(ResourceLocation location, JsonObject object,
				LootItemCondition[] lootConditions) 
		{
			Item skullItem = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryBuild(GsonHelper.getAsString(object, "skull")));
			return new DecapitateLootModifier(lootConditions, skullItem);
		}

		@Override
		public JsonObject write(DecapitateLootModifier instance)
		{
			JsonObject result = this.makeConditions(instance.conditions);
			result.addProperty("skull", ForgeRegistries.ITEMS.getKey(instance.skull).toString());
			return result;
		}
		
	}*/

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() 
	{
		return DECAPITATE_CODEC;
	}
}
