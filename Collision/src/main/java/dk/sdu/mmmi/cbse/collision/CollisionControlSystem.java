package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.Entity.Type;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class CollisionControlSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity a = entities.get(i);
                Entity b = entities.get(j);
                if (collides(a, b)) {
                    resolve(a, b, world);
                }
            }
        }
    }

    private boolean collides(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (a.getRadius() + b.getRadius());
    }

    private void resolve(Entity a, Entity b, World world) {

        if (isPair(a, b, Type.PLAYER, Type.ASTEROID)) {
            Entity player = typeOf(a, b, Type.PLAYER);
            Entity asteroid = typeOf(a, b, Type.ASTEROID);
            player.addHit();
            world.removeEntity(asteroid);
            return;
        }

        if (a.getType() == Type.ASTEROID && b.getType() == Type.ASTEROID) {
            bounce(a, b);
            return;
        }

        if (isPair(a, b, Type.BULLET, Type.PLAYER)) {
            Entity bullet = typeOf(a, b, Type.BULLET);
            Entity player = typeOf(a, b, Type.PLAYER);
            if (bullet.getFiredBy() == Type.ENEMY) {
                player.addHit();
                world.removeEntity(bullet);
            }
            return;
        }

        if (isPair(a, b, Type.BULLET, Type.ASTEROID)) {
            Entity bullet = typeOf(a, b, Type.BULLET);
            Entity asteroid = typeOf(a, b, Type.ASTEROID);
            if (bullet.getFiredBy() == Type.PLAYER) {
                asteroid.addHit();
                world.removeEntity(bullet);
            }
            return;
        }

        if (isPair(a, b, Type.BULLET, Type.ENEMY)) {
            Entity bullet = typeOf(a, b, Type.BULLET);
            Entity enemy = typeOf(a, b, Type.ENEMY);
            if (bullet.getFiredBy() == Type.PLAYER) {
                enemy.addHit();
                world.removeEntity(bullet);
            }
            return;
        }

    }

    private void bounce(Entity a, Entity b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) {
            return;
        }
        double nx = dx / distance;
        double ny = dy / distance;

        double relativeVelocity = (a.getDx() - b.getDx()) * nx + (a.getDy() - b.getDy()) * ny;
        if (relativeVelocity <= 0) {
            return;
        }

        a.setDx(a.getDx() - relativeVelocity * nx);
        a.setDy(a.getDy() - relativeVelocity * ny);
        b.setDx(b.getDx() + relativeVelocity * nx);
        b.setDy(b.getDy() + relativeVelocity * ny);

        double overlap = (a.getRadius() + b.getRadius()) - distance;
        if (overlap > 0) {
            a.setX(a.getX() - nx * overlap / 2);
            a.setY(a.getY() - ny * overlap / 2);
            b.setX(b.getX() + nx * overlap / 2);
            b.setY(b.getY() + ny * overlap / 2);
        }
    }

    private boolean isPair(Entity a, Entity b, Type t1, Type t2) {
        return (a.getType() == t1 && b.getType() == t2)
                || (a.getType() == t2 && b.getType() == t1);
    }

    private Entity typeOf(Entity a, Entity b, Type type) {
        return a.getType() == type ? a : b;
    }
}
