module dk.sdu.mmmi.cbse.bullet {
    requires dk.sdu.mmmi.cbse.common;

    provides dk.sdu.mmmi.cbse.common.services.IEntityProcessingService
            with dk.sdu.mmmi.cbse.bullet.BulletControlSystem;
    provides dk.sdu.mmmi.cbse.common.services.BulletSPI
            with dk.sdu.mmmi.cbse.bullet.BulletSPIImpl;
}
