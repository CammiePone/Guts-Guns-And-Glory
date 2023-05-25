package dev.cammiescorner.guts_guns_glory.common.items;

import dev.cammiescorner.guts_guns_glory.common.registry.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class BandageItem extends Item {
	public BandageItem() {
		super(new QuiltItemSettings().maxCount(16));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(user.hasStatusEffect(ModStatusEffects.BLEED))
			return ItemUsage.consumeHeldItem(world, user, hand);

		return TypedActionResult.fail(user.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(user instanceof PlayerEntity player) {
			if(!player.isCreative())
				stack.decrement(1);

			int amplifier = player.getStatusEffect(ModStatusEffects.BLEED).getAmplifier();

			user.removeStatusEffect(ModStatusEffects.BLEED);

			if(amplifier > 0)
				user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLEED, Integer.MAX_VALUE, amplifier - 1, true, false));
		}

		return stack;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}
}
