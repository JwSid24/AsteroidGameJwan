module dk.sdu.mmmi.cbse.collision {
    requires dk.sdu.mmmi.cbse.common;

    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collision.CollisionControlSystem;
}
