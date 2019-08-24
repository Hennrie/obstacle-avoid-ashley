package com.obstacleavoid.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.GameConfig;

public class Obstacle extends GameObjectClass implements Pool.Poolable {

    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;


    public Obstacle() {
        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }

    public void updatePosition() {
        setY(getY() - ySpeed);
    }

    public float getWidth() {
        return GameConfig.OBSTACLE_SIZE;
    }

    public float getHeight() { return GameConfig.OBSTACLE_SIZE; }
    public boolean isPlayerColliding(Player player) {

        Circle playerBounds = player.getBounds();

        // check if playerBounds overlap obstacle bounds
        boolean overlaps = Intersector.overlaps(playerBounds, getBounds());

//        if(overlaps) {
//            hit = true;
//        }

        // better way

        hit = overlaps;

        return overlaps;
    }

    public boolean isNotHit() { return !hit;}

    public void setYSpeed(float obstacleSpeed) {
        this.ySpeed = obstacleSpeed;
    }

    public void setRadius(float radius) {
        GameConfig.OBSTACLE_BOUNDS_RADIUS = radius;
    }

    @Override
    public void reset() {
        hit = false;
    }



}
