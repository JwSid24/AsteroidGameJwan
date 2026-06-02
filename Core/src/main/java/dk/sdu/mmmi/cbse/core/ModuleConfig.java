package dk.sdu.mmmi.cbse.core;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.ScoreSPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Configuration
class ModuleConfig {

    public ModuleConfig() {
    }

    @Bean
    public Game game() {
        return new Game(
                gamePluginServices(),
                entityProcessingServices(),
                postEntityProcessingServices(),
                ServiceLoader.load(ScoreSPI.class).findFirst().orElse(null));
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return discover(IGamePluginService.class);
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServices() {
        return discover(IEntityProcessingService.class);
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return discover(IPostEntityProcessingService.class);
    }

    private static <T> List<T> discover(Class<T> service) {
        List<T> all = new ArrayList<>();
        ServiceLoader.load(service).forEach(all::add);
        all.addAll(LayerLoader.load(service));
        return all;
    }
}
