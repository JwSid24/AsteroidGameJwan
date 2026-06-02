module dk.sdu.mmmi.cbse.enemy {
    requires dk.sdu.mmmi.cbse.common;

    uses dk.sdu.mmmi.cbse.common.services.BulletSPI;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.enemy.EnemyPlugin;
    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.enemy.EnemyControlSystem;
}
