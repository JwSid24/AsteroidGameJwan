package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> offScreen = new ArrayList<>();
        for (Entity bullet : world.getEntities()) {
            if (bullet.getType() != Entity.Type.BULLET) {
                continue;
            }
            bullet.setX(bullet.getX() + bullet.getDx());
            bullet.setY(bullet.getY() + bullet.getDy());
            if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                    || bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight()) {
                offScreen.add(bullet);
            }
        }
        offScreen.forEach(world::removeEntity);
    }
}
