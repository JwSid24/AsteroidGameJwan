package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        world.addEntity(Enemies.create(150, 150, random));
    }

    @Override
    public void stop(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities()) {
            if (e.getType() == Entity.Type.ENEMY) {
                toRemove.add(e);
            }
        }
        toRemove.forEach(world::removeEntity);
    }
}
