package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.Random;

final class Asteroids {

    private static final int VERTICES = 10;

    private Asteroids() {
    }

    static Entity create(Entity.Size size, double x, double y, double dx, double dy, Random random) {
        Entity asteroid = new Entity();
        asteroid.setType(Entity.Type.ASTEROID);
        asteroid.setSize(size);
        double radius = (size == Entity.Size.LARGE) ? 24 : 12;
        asteroid.setRadius(radius);
        asteroid.setPolygonCoordinates(jaggedShape(radius, random));
        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setDx(dx);
        asteroid.setDy(dy);
        asteroid.setRotation(random.nextDouble() * 360);
        return asteroid;
    }

    private static double[] jaggedShape(double radius, Random random) {
        double[] coords = new double[VERTICES * 2];
        for (int i = 0; i < VERTICES; i++) {
            double angle = 2 * Math.PI * i / VERTICES;
            double r = radius * (0.7 + random.nextDouble() * 0.3);
            coords[i * 2] = Math.cos(angle) * r;
            coords[i * 2 + 1] = Math.sin(angle) * r;
        }
        return coords;
    }
}
