package io.github.vampirestudios.raa.items;

import io.github.vampirestudios.raa.materials.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class RAASwordItem extends SwordItem {

    public Material material;

    public RAASwordItem(Material material, Settings settings) {
        super(material.getToolMaterial(), (int) material.getToolMaterial().getSwordAttackDamage(), -2.4F, settings);
        this.material = material;
    }

    @Override
    public Text getName(ItemStack itemStack_1) {
        return new TranslatableText("text.raa.item.sword", new LiteralText(material.getName()));
    }

}
