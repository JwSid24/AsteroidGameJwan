package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidProcessingSystem implements IEntityProcessingService {

    private static final double SPLIT_SPEED = 1.2;
    private static final double SPEED = 0.8;
    private static final int MAX_ASTEROIDS = 8;
    private static final double SPAWN_CHANCE = 0.015;

    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        int asteroidCount = 0;

        for (Entity asteroid : world.getEntities()) {
            if (asteroid.getType() != Entity.Type.ASTEROID) {
                continue;
            }
            asteroidCount++;

            if (asteroid.getSize() == Entity.Size.LARGE && asteroid.getHitsTaken() >= 2) {
                gameData.addAsteroidKill(1.0);
                splitInTwo(asteroid, world);
                world.removeEntity(asteroid);
                continue;
            }
            if (asteroid.getSize() == Entity.Size.SMALL && asteroid.getHitsTaken() >= 1) {
                gameData.addAsteroidKill(0.5);
                world.removeEntity(asteroid);
                continue;
            }

            asteroid.setX(asteroid.getX() + asteroid.getDx());
            asteroid.setY(asteroid.getY() + asteroid.getDy());
            asteroid.setRotation(asteroid.getRotation() + 0.5);
            wrap(asteroid, gameData);
        }

        if (asteroidCount < MAX_ASTEROIDS && random.nextDouble() < SPAWN_CHANCE) {
            spawnFromEdge(gameData, world);
        }
    }

    private void splitInTwo(Entity parent, World world) {
        double angle = random.nextDouble() * Math.PI * 2;
        world.addEntity(Asteroids.create(Entity.Size.SMALL,
                parent.getX(), parent.getY(),
                Math.cos(angle) * SPLIT_SPEED, Math.sin(angle) * SPLIT_SPEED, random));
        world.addEntity(Asteroids.create(Entity.Size.SMALL,
                parent.getX(), parent.getY(),
                -Math.cos(angle) * SPLIT_SPEED, -Math.sin(angle) * SPLIT_SPEED, random));
    }

    private void spawnFromEdge(GameData g, World world) {
        double x;
        double y;
        switch (random.nextInt(4)) {
            case 0 -> { x = 0;                  y = random.nextDouble() * g.getDisplayHeight(); }
            case 1 -> { x = g.getDisplayWidth(); y = random.nextDouble() * g.getDisplayHeight(); }
            case 2 -> { x = random.nextDouble() * g.getDisplayWidth(); y = 0; }
            default -> { x = random.nextDouble() * g.getDisplayWidth(); y = g.getDisplayHeight(); }
        }
        double angle = random.nextDouble() * Math.PI * 2;
        world.addEntity(Asteroids.create(Entity.Size.LARGE, x, y,
                Math.cos(angle) * SPEED, Math.sin(angle) * SPEED, random));
    }

    private void wrap(Entity e, GameData g) {
        if (e.getX() < 0) e.setX(e.getX() + g.getDisplayWidth());
        if (e.getX() > g.getDisplayWidth()) e.setX(e.getX() - g.getDisplayWidth());
        if (e.getY() < 0) e.setY(e.getY() + g.getDisplayHeight());
        if (e.getY() > g.getDisplayHeight()) e.setY(e.getY() - g.getDisplayHeight());
    }
}
