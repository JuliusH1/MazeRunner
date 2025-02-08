package me.JuliusH_1.mazerunners.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import me.JuliusH_1.mazerunners.entities.Entity;

public abstract class EntityLiving extends Entity {

    public EntityLiving(World world) {
        super(world);
    }

    protected void initAttributes() {
        // Initialize default attributes for living entities
    }

    public void doHurtTarget(Entity target) {
        // Logic for hurting the target entity
    }

    public Entity getTarget() {
        // Logic to get the current target of the entity
        return null;
    }

    public void setTarget(Entity target) {
        // Logic to set the current target of the entity
    }

    public void getNavigation() {
        // Logic to get the entity's navigation system
    }
}