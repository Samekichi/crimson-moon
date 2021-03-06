package draylar.crimsonmoon.item;

import draylar.crimsonmoon.api.AttackingItem;
import draylar.crimsonmoon.material.CrimsonToolMaterial;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CarnageItem extends ToolItem implements AttackingItem {

    public CarnageItem(Settings settings) {
        super(CrimsonToolMaterial.INSTANCE, settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return ((TranslatableText) super.getName(stack)).formatted(Formatting.DARK_RED);
    }

    @Override
    public void attack(PlayerEntity player, World world, ItemStack stack) {
        if(!world.isClient) {
            Vec3d rotationVector = player.getRotationVector();
            Vec3d pos = player.getPos().add(rotationVector.multiply(2)).add(0, player.getEyeHeight(player.getPose()), 0);

            world.getEntitiesByClass(LivingEntity.class, new Box(pos.x - 1, pos.y - .75, pos.z - 1, pos.x + 1, pos.y + .5, pos.z + 1), entity -> !entity.equals(player)).forEach(entity -> {
                entity.damage(DamageSource.player(player), EnchantmentHelper.getAttackDamage(stack, entity.getGroup()) + 5);

                if(entity.isDead()) {
                    player.heal(2f);
                }
            });
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("Crush my foes!").setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.GRAY)));
        tooltip.add(LiteralText.EMPTY);
        tooltip.add(new LiteralText("When in Main Hand: ").formatted(Formatting.GRAY));
        tooltip.add(new LiteralText(" 5 Attack Damage").formatted(Formatting.DARK_GREEN));
        tooltip.add(new LiteralText(" Auto-swing").formatted(Formatting.DARK_GREEN));
    }
}
