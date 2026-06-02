package dk.sdu.mmmi.cbse.common.data;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        entityMap.put(entity.getId(), entity);
        return entity.getId();
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getId());
    }

    public void removeEntity(String id) {
        entityMap.remove(id);
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }
}
