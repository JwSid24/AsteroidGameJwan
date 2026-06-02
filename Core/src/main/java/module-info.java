module dk.sdu.mmmi.cbse.core {
    requires javafx.graphics;
    requires dk.sdu.mmmi.cbse.common;
    requires spring.context;

    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.ScoreSPI;

    exports dk.sdu.mmmi.cbse.core;

    opens dk.sdu.mmmi.cbse.core to spring.core, spring.beans, spring.context;
}
