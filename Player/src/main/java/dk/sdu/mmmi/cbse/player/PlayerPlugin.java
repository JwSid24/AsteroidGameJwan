package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {

    private Entity player;

    @Override
    public void start(GameData gameData, World world) {
        player = new Entity();
        player.setType(Entity.Type.PLAYER);

        player.setPolygonCoordinates(0, -22, -9, 15, 0, 8, 9, 15);
        player.setX(gameData.getDisplayWidth() / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        player.setRadius(15);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {
        if (player != null) {
            world.removeEntity(player);
            player = null;
        }
    }
}
