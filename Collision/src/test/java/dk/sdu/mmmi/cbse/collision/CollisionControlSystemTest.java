package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollisionControlSystemTest {

    private final CollisionControlSystem collision = new CollisionControlSystem();

    @Test
    void playerHitByAsteroid_takesOneHitAndAsteroidIsRemoved() {
        World world = new World();
        GameData gameData = new GameData();

        Entity player = entity(Entity.Type.PLAYER, 100, 100, 15);
        world.addEntity(player);
        Entity asteroid = entity(Entity.Type.ASTEROID, 105, 100, 20);
        asteroid.setSize(Entity.Size.LARGE);
        world.addEntity(asteroid);

        collision.process(gameData, world);

        assertEquals(1, player.getHitsTaken(), "player takes exactly one hit from an asteroid");
        assertFalse(world.getEntities().contains(asteroid), "asteroid is destroyed on impact");
    }

    @Test
    void playerFarFromAsteroid_isUnharmed() {
        World world = new World();
        GameData gameData = new GameData();

        Entity player = entity(Entity.Type.PLAYER, 100, 100, 15);
        world.addEntity(player);
        Entity asteroid = entity(Entity.Type.ASTEROID, 500, 500, 20);
        world.addEntity(asteroid);

        collision.process(gameData, world);

        assertEquals(0, player.getHitsTaken(), "no hit when nothing overlaps");
        assertTrue(world.getEntities().contains(asteroid), "distant asteroid is untouched");
    }

    @Test
    void playerBulletHitsAsteroid_asteroidTakesHitAndBulletConsumed() {
        World world = new World();
        GameData gameData = new GameData();

        Entity asteroid = entity(Entity.Type.ASTEROID, 200, 200, 20);
        asteroid.setSize(Entity.Size.LARGE);
        world.addEntity(asteroid);
        Entity bullet = entity(Entity.Type.BULLET, 205, 200, 3);
        bullet.setFiredBy(Entity.Type.PLAYER);
        world.addEntity(bullet);

        collision.process(gameData, world);

        assertEquals(1, asteroid.getHitsTaken(), "player bullet damages the asteroid");
        assertFalse(world.getEntities().contains(bullet), "bullet is consumed on impact");
    }

    @Test
    void twoAsteroidsCollideHeadOn_bounceApart() {
        World world = new World();
        GameData gameData = new GameData();

        Entity a = entity(Entity.Type.ASTEROID, 100, 100, 20);
        a.setDx(1);
        world.addEntity(a);
        Entity b = entity(Entity.Type.ASTEROID, 130, 100, 20);
        b.setDx(-1);
        world.addEntity(b);

        collision.process(gameData, world);

        assertTrue(a.getDx() < 0, "asteroid A bounces back to the left");
        assertTrue(b.getDx() > 0, "asteroid B bounces back to the right");
    }

    private static Entity entity(Entity.Type type, double x, double y, double radius) {
        Entity e = new Entity();
        e.setType(type);
        e.setX(x);
        e.setY(y);
        e.setRadius(radius);
        return e;
    }
}
