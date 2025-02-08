package me.JuliusH_1.mazerunners.entities;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.World;
import me.JuliusH_1.mazerunners.entities.EntityLiving;

import java.util.EnumSet;

public abstract class EntityInsentient extends EntityLiving {

    public EntityInsentient(World world) {
        super(world);
        this.initGoals();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        // Initialize default attributes for insentient entities
    }

    protected void initGoals() {
        this.goalSelector.addGoal(0, new WanderGoal(this));
        this.goalSelector.addGoal(1, new AttackGoal(this));
        this.goalSelector.addGoal(2, new FollowPlayerGoal(this));
        this.goalSelector.addGoal(3, new AvoidObstacleGoal(this));
    }

    static class WanderGoal extends Goal {
        private final EntityInsentient entity;

        public WanderGoal(EntityInsentient entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            // Logic to determine if the entity should start wandering
            return true;
        }

        @Override
        public void tick() {
            // Logic for wandering behavior
            this.entity.getNavigation().moveTo(this.entity.getRandom().nextDouble() * 10, this.entity.getRandom().nextDouble() * 10, this.entity.getRandom().nextDouble() * 10, 1.0);
        }
    }

    static class AttackGoal extends Goal {
        private final EntityInsentient entity;

        public AttackGoal(EntityInsentient entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            // Logic to determine if the entity should start attacking
            return this.entity.getTarget() != null;
        }

        @Override
        public void tick() {
            // Logic for attacking behavior
            if (this.entity.getTarget() != null) {
                this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.0);
                this.entity.doHurtTarget(this.entity.getTarget());
            }
        }
    }

    static class FollowPlayerGoal extends Goal {
        private final EntityInsentient entity;

        public FollowPlayerGoal(EntityInsentient entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            // Logic to determine if the entity should start following a player
            return this.entity.getTarget() != null && this.entity.getTarget() instanceof Player;
        }

        @Override
        public void tick() {
            // Logic for following player behavior
            if (this.entity.getTarget() != null) {
                this.entity.getNavigation().moveTo(this.entity.getTarget(), 1.0);
            }
        }
    }

    static class AvoidObstacleGoal extends Goal {
        private final EntityInsentient entity;

        public AvoidObstacleGoal(EntityInsentient entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            // Logic to determine if the entity should start avoiding obstacles
            return true;
        }

        @Override
        public void tick() {
            // Logic for avoiding obstacles
            // Implement obstacle detection and avoidance logic here
        }
    }
}