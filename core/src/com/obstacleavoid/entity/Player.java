package com.obstacleavoid.entity;

import com.obstacleavoid.config.GameConfig;

public class Player extends GameObjectClass {

    public Player() {

        super(GameConfig.PLAYER_BOUNDS_RADIUS);
        setSize(GameConfig.PLAYER_SIZE, GameConfig.PLAYER_SIZE);
    }

}
