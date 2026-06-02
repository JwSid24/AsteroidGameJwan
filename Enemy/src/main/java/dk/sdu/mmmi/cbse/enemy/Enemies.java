package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.Random;

final class Enemies {

    private Enemies() {
    }

    static Entity create(double x, double y, Random random) {
        Entity enemy = new Entity();
        enemy.setType(Entity.Type.ENEMY);
        enemy.setPolygonCoordinates(0, -16, -12, 12, 12, 12);
        enemy.setX(x);
        enemy.setY(y);
        enemy.setRadius(14);
        enemy.setRotation(random.nextDouble() * 360);
        return enemy;
    }
}
