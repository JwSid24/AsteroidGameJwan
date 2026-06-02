package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.BulletSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessingService {

    private static final double ROTATION_SPEED = 2.5;
    private static final double THRUST = 0.08;
    private static final double FRICTION = 0.985;
    private static final int SHOOT_COOLDOWN = 12;
    private static final int MAX_HITS = 5;

    private BulletSPI bulletSPI;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities()) {
            if (player.getType() != Entity.Type.PLAYER) {
                continue;
            }

            if (player.getHitsTaken() >= MAX_HITS) {
                world.removeEntity(player);
                continue;
            }

            GameKeys keys = gameData.getKeys();
            if (keys.isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - ROTATION_SPEED);
            }
            if (keys.isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + ROTATION_SPEED);
            }
            if (keys.isDown(GameKeys.UP)) {
                double rad = Math.toRadians(player.getRotation());
                player.setDx(player.getDx() + Math.sin(rad) * THRUST);
                player.setDy(player.getDy() - Math.cos(rad) * THRUST);
            }
            if (keys.isDown(GameKeys.SPACE) && player.getShootCooldown() <= 0) {
                BulletSPI spi = bulletSPI();
                if (spi != null) {
                    world.addEntity(spi.createBullet(player, gameData));
                    player.setShootCooldown(SHOOT_COOLDOWN);
                }
            }
            if (player.getShootCooldown() > 0) {
                player.setShootCooldown(player.getShootCooldown() - 1);
            }

            player.setDx(player.getDx() * FRICTION);
            player.setDy(player.getDy() * FRICTION);
            player.setX(player.getX() + player.getDx());
            player.setY(player.getY() + player.getDy());
            wrap(player, gameData);
        }
    }

    private void wrap(Entity e, GameData g) {
        if (e.getX() < 0) e.setX(e.getX() + g.getDisplayWidth());
        if (e.getX() > g.getDisplayWidth()) e.setX(e.getX() - g.getDisplayWidth());
        if (e.getY() < 0) e.setY(e.getY() + g.getDisplayHeight());
        if (e.getY() > g.getDisplayHeight()) e.setY(e.getY() - g.getDisplayHeight());
    }

    private BulletSPI bulletSPI() {
        if (bulletSPI == null) {
            bulletSPI = ServiceLoader.load(BulletSPI.class).findFirst().orElse(null);
        }
        return bulletSPI;
    }
}
