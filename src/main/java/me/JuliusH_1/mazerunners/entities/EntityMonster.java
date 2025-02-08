package me.JuliusH_1.mazerunners.entities;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.level.World;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import me.JuliusH_1.mazerunners.entities.EntityInsentient;

public abstract class EntityMonster extends EntityInsentient {

    public EntityMonster(World world) {
        super(world);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.initAttributes(GenericAttributes.ATTACK_DAMAGE).setValue(4.0); // Default attack damage
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