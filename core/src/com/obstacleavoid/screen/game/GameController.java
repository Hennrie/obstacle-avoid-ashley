package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.common.GameManager;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;
@Deprecated

public class GameController {

    // == constants ==
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);


    // == variables ==

    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Obstacle> obstacles2 = new Array<Obstacle>();
    private Background background;

    private float obstacleTime;
    private float scoreTimer;
    public int lives = GameConfig.LIVES_START;
    public int score;
    public int displayScore;
    private int i;
    private Sound hit;

    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;
    private Pool<Obstacle> obstaclePool;
    private Pool<Obstacle> obstaclePool2;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private final float halfPlayerSize = GameConfig.PLAYER_SIZE / 2;
    private final float startPlayerX = GameConfig.WORLD_WIDTH / 2f - halfPlayerSize;
    private final float startPlayerY = 1 - halfPlayerSize;

    // == constructors ==

    public GameController(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }

    private void init() {

        // create player
        player = new Player();

        // position player
        player.setPosition(startPlayerX, startPlayerY);

        // create obstacle pool

        obstaclePool = Pools.get(Obstacle.class, 4);
        obstaclePool2 = Pools.get(Obstacle.class,5);

        // create background

        background = new Background();
        background.setPosition(0,0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        hit = assetManager.get(AssetDescriptors.HIT_SOUND);
    }

    // == public methods ==

    public boolean isGameOver(){

        return lives <= 0;
    }

    public void update(float delta) {

        if(isGameOver()){
            return;
        }

        updatePlayer();
        updateObstacle(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if(isPlayerCollidingWithObstacle()) {
            log.debug("Collision detected");
            lives --;

            if(isGameOver()) {
                log.debug("Game Over");
                GameManager.INSTANCE.updateHighScore(score);
            } else {
                restart();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Array<Obstacle> getObstacles2() {
        return obstacles2;
    }

    public Background getBackground() {
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getdisplayScore() {
        return displayScore;
    }

    public void setI(int i) {
        this.i = i;
    }

    // == private methods ==

    private void restart() {
        obstaclePool.freeAll(obstacles);
        obstaclePool2.freeAll(obstacles2);
        obstacles.clear();
        obstacles2.clear();
        player.setPosition(startPlayerX, startPlayerY);
        i = 0;
    }

    private boolean isPlayerCollidingWithObstacle() {

        for(Obstacle obstacle : obstacles) {
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                hit.play();
                return true;
            }
        }

        for(Obstacle obstacle : obstacles2) {
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)) {
                return true;
            }
        }

        return false;
    }

    private void updatePlayer() {
        float xSpeed = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
        }

        player.setX(player.getX() + xSpeed);


        blockPlayerFromLeavingTheWorld();
    }

    private void blockPlayerFromLeavingTheWorld() {
        float playerX = MathUtils.clamp(player.getX(),
                0,
                GameConfig.WORLD_WIDTH - player.getWidth());

//        if(playerX < player.getWidth() / 2f) {
//            playerX = player.getWidth() / 2f;
//        } else if(playerX > GameConfig.WORLD_WIDTH - player.getWidth() / 2f) {
//            playerX = GameConfig.WORLD_WIDTH - player.getWidth() / 2f;
//        }
//
        player.setPosition(playerX, player.getY());
    }

    private void updateObstacle(float delta) {
        for(Obstacle obstacle : obstacles) {
            obstacle.updatePosition();
        }

        for(Obstacle obstacle : obstacles2) {
            obstacle.updatePosition();
        }

        createNewObstacle(delta);
        removePassedObstacles();
        removePassedObstacles2();
    }

    private void createNewObstacle(float delta) {
    obstacleTime += delta;


        if(obstacleTime > GameConfig.OBSTACLE_SPAWN_TIME) {


            setI(i + 1);
            float min = 0;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max);
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = obstaclePool.obtain();
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            log.debug(String.valueOf(i));

            if(i % 5 == 0 )  {


                log.debug("jo radiuuuuuuuuuuus");
                obstacle.setRadius(0.6f);
                Obstacle obstacle2 = obstaclePool2.obtain();
                obstacle2.setYSpeed(difficultyLevel.getObstacleSpeed());
                obstacle2.setPosition(obstacleX, obstacleY);
                obstacles2.add(obstacle2);
                obstacleTime = 0f;

            } else if (i % 11 == 0) {
                obstacle.setRadius(1.0f);
                obstacles.add(obstacle);
                obstacleTime = 0f;
            } else {
                obstacles.add(obstacle);
                obstacleTime = 0f;
            }
        }
    }

    private void removePassedObstacles() {
        if(obstacles.size > 0) {
            Obstacle first = obstacles.first();
            float minObstacleY = - GameConfig.OBSTACLE_SIZE;

            if(first.getY() < minObstacleY) {
                obstacles.removeValue(first, true);
                obstaclePool.free(first);

            }
        }
    }

    private void removePassedObstacles2 () {
        if(obstacles2.size > 0) {
            Obstacle first = obstacles2.first();
            float minObstacleY = - GameConfig.OBSTACLE_SIZE;

            if(first.getY() < minObstacleY) {
                obstacles2.removeValue(first, true);
                obstaclePool2.free(first);
            }
        }

    }



    private void updateScore(float delta){
        scoreTimer += delta;

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            score += MathUtils.random(1,5);
            scoreTimer = 0.0f;
        }
    }

    private void updateDisplayScore (float delta) {
        if(displayScore < score) {
            displayScore = Math.min(
                    score,
                    displayScore + (int) (60* delta)
            );
        }
    }


}
