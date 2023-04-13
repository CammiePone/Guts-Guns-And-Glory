package dev.cammiescorner.guts_guns_glory.common.items;

import dev.cammiescorner.guts_guns_glory.common.registry.ModComponents;
import dev.cammiescorner.guts_guns_glory.common.registry.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class BloodBagItem extends Item {
	private final boolean isFilled;

	public BloodBagItem(boolean isFilled) {
		super(new QuiltItemSettings().maxCount(16));
		this.isFilled = isFilled;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(user.isCreative() || (isFilled ? ModComponents.getBlood(user) < ModComponents.getMaxBlood(user) : ModComponents.getBlood(user) >= 10))
			return ItemUsage.consumeHeldItem(world, user, hand);

		return TypedActionResult.fail(user.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(user instanceof PlayerEntity player) {
			Item bloodBagItem = isFilled ? ModItems.EMPTY_BLOOD_BAG : ModItems.BLOOD_BAG;

			if(!player.isCreative())
				stack.decrement(1);

			if(isFilled)
				ModComponents.BLOOD.get(player).incrementBlood(10, false);
			else if(!player.isCreative())
				ModComponents.decrementBlood(player, 10, false);

			if(stack.isEmpty())
				return new ItemStack(bloodBagItem);

			player.getInventory().insertStack(new ItemStack(bloodBagItem));
		}

		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return isFilled ? UseAction.DRINK : UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}
}
