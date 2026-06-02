package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    private static final int INITIAL_COUNT = 4;
    private static final double SPEED = 0.8;

    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < INITIAL_COUNT; i++) {
            double x = random.nextDouble() * gameData.getDisplayWidth();
            double y = random.nextDouble() * gameData.getDisplayHeight();
            double angle = random.nextDouble() * Math.PI * 2;
            world.addEntity(Asteroids.create(
                    Entity.Size.LARGE, x, y,
                    Math.cos(angle) * SPEED, Math.sin(angle) * SPEED, random));
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities()) {
            if (e.getType() == Entity.Type.ASTEROID) {
                toRemove.add(e);
            }
        }
        toRemove.forEach(world::removeEntity);
    }
}
