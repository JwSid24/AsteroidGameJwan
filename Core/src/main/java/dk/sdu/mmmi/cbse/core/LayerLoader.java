package dk.sdu.mmmi.cbse.core;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public final class LayerLoader {

    private static final Path PLUGINS_DIR = Paths.get("plugins");

    private static boolean built = false;
    private static ModuleLayer pluginLayer;

    private LayerLoader() {
    }

    public static <T> List<T> load(Class<T> service) {
        ModuleLayer layer = layer();
        if (layer == null) {
            return List.of();
        }
        List<T> result = new ArrayList<>();
        ServiceLoader.load(layer, service).stream()
                .filter(p -> p.type().getModule().getLayer() == layer)
                .map(ServiceLoader.Provider::get)
                .forEach(result::add);
        return result;
    }

    private static synchronized ModuleLayer layer() {
        if (built) {
            return pluginLayer;
        }
        built = true;

        if (!Files.isDirectory(PLUGINS_DIR)) {
            return null;
        }
        ModuleFinder finder = ModuleFinder.of(PLUGINS_DIR);
        Set<String> moduleNames = finder.findAll().stream()
                .map(ref -> ref.descriptor().name())
                .collect(Collectors.toSet());
        if (moduleNames.isEmpty()) {
            return null;
        }

        ModuleLayer parent = ModuleLayer.boot();
        Configuration cf = parent.configuration()
                .resolve(finder, ModuleFinder.of(), moduleNames);
        pluginLayer = parent.defineModulesWithOneLoader(cf, ClassLoader.getSystemClassLoader());
        return pluginLayer;
    }
}
