package me.JuliusH_1.mazerunners.entities;

import net.minecraft.world.level.World;

public abstract class Entity {

    protected World world;

    public Entity(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void tick() {
        // Logic for updating the entity each tick
    }

    public void remove() {
        // Logic for removing the entity from the world
    }

    public boolean isAlive() {
        // Logic to check if the entity is alive
        return true;
    }

    public void move(double x, double y, double z) {
        // Logic for moving the entity
    }

    public void getNavigation() {
        // Logic to get the entity's navigation system
    }
}