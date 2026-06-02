package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.BulletSPI;

public class BulletSPIImpl implements BulletSPI {

    private static final double BULLET_SPEED = 6.0;

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Entity();
        bullet.setType(Entity.Type.BULLET);
        bullet.setFiredBy(shooter.getType());

        double rad = Math.toRadians(shooter.getRotation());
        double nose = shooter.getRadius() + 5;
        bullet.setX(shooter.getX() + Math.sin(rad) * nose);
        bullet.setY(shooter.getY() - Math.cos(rad) * nose);
        bullet.setDx(Math.sin(rad) * BULLET_SPEED);
        bullet.setDy(-Math.cos(rad) * BULLET_SPEED);
        bullet.setRadius(3);

        bullet.setPolygonCoordinates(-2, -2, 2, -2, 2, 2, -2, 2);
        return bullet;
    }
}
