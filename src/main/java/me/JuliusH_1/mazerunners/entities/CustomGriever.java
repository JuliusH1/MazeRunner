package me.JuliusH_1.mazerunners.entities;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.level.World;
import org.bukkit.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import me.JuliusH_1.mazerunners.entities.EntityMonster;

public class CustomGriever extends EntityMonster {

    public CustomGriever(World world) {
        super(world);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0); // Custom damage
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25); // Custom speed
    }

    @Override
    public void damageEntity(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            damageEvent.setDamage(DamageModifier.BASE, damageEvent.getDamage(DamageModifier.BASE) * 1.5);
        }
        super.damageEntity(event);
    }
}