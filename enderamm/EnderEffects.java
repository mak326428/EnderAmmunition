package enderamm;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thermalexpansion.fluid.TEFluids;
import cpw.mods.fml.common.registry.GameRegistry;
import enderamm.item.ItemAnnihilationManipulator.AnnihilationDamageSource;
import enderamm.item.ItemEnderArrow;
import enderamm.item.ItemEnderArrow.IEnderEffect;

public class EnderEffects {
	public static void init() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(
				EACommonProxy.itemEnderArrow), new ItemStack(
				EACommonProxy.itemMaterial, 1, 0), new ItemStack(Item.arrow),
				TEFluids.bucketPyrotheum));
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {

			@Override
			public String getName() {
				return EnumChatFormatting.DARK_GREEN + "Poisonous";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				if (e instanceof EntityLivingBase) {
					((EntityLivingBase) e).addPotionEffect(new PotionEffect(
							Potion.poison.id, 10 * 20, 5, false));
				}
			}

			@Override
			public void addRecipe(ItemStack target) {
				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Item.potion, 1, 8116), 'G',
						new ItemStack(Block.glowStone)));
			}
		});
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {
			@Override
			public String getName() {
				return EnumChatFormatting.DARK_PURPLE + "Withering";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				e.attackEntityFrom(DamageSource.wither, 5.0F);
				if (e instanceof EntityLivingBase) {
					((EntityLivingBase) e).addPotionEffect(new PotionEffect(
							Potion.wither.id, 10 * 20, 5, false));
				}
			}

			@Override
			public void addRecipe(ItemStack target) {

				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Item.skull, 1, 1), 'G', new ItemStack(
								Block.glowStone)));
			}
		});
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {
			@Override
			public String getName() {
				return EnumChatFormatting.YELLOW + "Piercing";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				e.attackEntityFrom(new AnnihilationDamageSource(shooter),
						new Random().nextInt(10) + 10);
			}

			@Override
			public void addRecipe(ItemStack target) {

				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Block.blockNetherQuartz), 'G',
						new ItemStack(Block.glowStone)));
			}
		});
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {
			@Override
			public String getName() {
				return EnumChatFormatting.RED + "Flammable";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				e.attackEntityFrom(DamageSource.onFire,
						new Random().nextInt(10) + 10);
				e.setFire(60);
			}

			@Override
			public void addRecipe(ItemStack target) {

				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Item.bucketLava), 'G',
						new ItemStack(Block.glowStone)));
			}
		});
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {
			@Override
			public String getName() {
				return EnumChatFormatting.BLUE + "Jump";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				Random rnd = new Random();
				//e.attackEntityFrom(DamageSource.fall,
				//		rnd.nextInt(10) + 10);
				e.motionX *= -rnd.nextInt(10);
				e.motionZ *= -rnd.nextInt(10);
				e.motionY += 5 + rnd.nextInt(15);
			}

			@Override
			public void addRecipe(ItemStack target) {

				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Item.ghastTear), 'G',
						new ItemStack(Block.glowStone)));
			}
		});
		ItemEnderArrow.registeredEffects.add(new IEnderEffect() {
			@Override
			public String getName() {
				return EnumChatFormatting.GRAY + "Explosive";
			}

			@Override
			public void apply(Entity e, EntityPlayer shooter,
					ItemStack arrowStack, World w) {
				w.createExplosion(null, e.posX, e.posY - 2.0D, e.posZ, 5.0F, true);
			}

			@Override
			public void addRecipe(ItemStack target) {

				GameRegistry.addRecipe(new ShapedOreRecipe(target, "AP", "GG",
						'A', new ItemStack(EACommonProxy.itemEnderArrow), 'P',
						new ItemStack(Block.tnt), 'G',
						new ItemStack(Block.glowStone)));
			}
		});
	}
}
