package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.BulletSPI;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;
import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double THRUST = 0.08;
    private static final double FRICTION = 0.985;
    private static final double TURN_SPEED = 1.6;
    private static final int MAX_HITS = 3;
    private static final double KILL_POINTS = 2.0;
    private static final int SHOOT_MIN = 70;
    private static final int SHOOT_VAR = 60;
    private static final double RESPAWN_CHANCE = 0.012;

    private final Random random = new Random();
    private BulletSPI bulletSPI;

    @Override
    public void process(GameData gameData, World world) {
        Entity player = findPlayer(world);
        int enemyCount = 0;

        for (Entity enemy : world.getEntities()) {
            if (enemy.getType() != Entity.Type.ENEMY) {
                continue;
            }
            enemyCount++;

            if (enemy.getHitsTaken() >= MAX_HITS) {
                world.removeEntity(enemy);
                gameData.addEnemyKill(KILL_POINTS);
                continue;
            }

            if (player != null) {

                double target = Math.toDegrees(Math.atan2(
                        player.getX() - enemy.getX(),
                        -(player.getY() - enemy.getY())));
                enemy.setRotation(turnToward(enemy.getRotation(), target, TURN_SPEED));
                double rad = Math.toRadians(enemy.getRotation());
                enemy.setDx(enemy.getDx() + Math.sin(rad) * THRUST);
                enemy.setDy(enemy.getDy() - Math.cos(rad) * THRUST);
            } else if (enemy.getActionTimer() <= 0) {

                enemy.setRotation(enemy.getRotation() + (random.nextDouble() - 0.5) * 90);
                enemy.setActionTimer(60 + random.nextInt(120));
            } else {
                enemy.setActionTimer(enemy.getActionTimer() - 1);
            }

            if (enemy.getShootCooldown() <= 0) {
                BulletSPI spi = bulletSPI();
                if (spi != null) {
                    world.addEntity(spi.createBullet(enemy, gameData));
                }
                enemy.setShootCooldown(SHOOT_MIN + random.nextInt(SHOOT_VAR));
            } else {
                enemy.setShootCooldown(enemy.getShootCooldown() - 1);
            }

            enemy.setDx(enemy.getDx() * FRICTION);
            enemy.setDy(enemy.getDy() * FRICTION);
            enemy.setX(enemy.getX() + enemy.getDx());
            enemy.setY(enemy.getY() + enemy.getDy());
            wrap(enemy, gameData);
        }

        if (enemyCount == 0 && random.nextDouble() < RESPAWN_CHANCE) {
            double x = random.nextBoolean() ? 0 : gameData.getDisplayWidth();
            double y = random.nextDouble() * gameData.getDisplayHeight();
            world.addEntity(Enemies.create(x, y, random));
        }
    }

    private Entity findPlayer(World world) {
        for (Entity e : world.getEntities()) {
            if (e.getType() == Entity.Type.PLAYER) {
                return e;
            }
        }
        return null;
    }

    private double turnToward(double current, double target, double maxStep) {
        double diff = ((target - current + 540) % 360) - 180;
        if (Math.abs(diff) <= maxStep) {
            return target;
        }
        return current + Math.signum(diff) * maxStep;
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
