module dk.sdu.mmmi.cbse.asteroid {
    requires dk.sdu.mmmi.cbse.common;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.asteroid.AsteroidProcessingSystem;
}
