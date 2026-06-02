package dk.sdu.mmmi.cbse.javalab;

import java.util.ServiceLoader;

public class Main {

    public static void main(String[] args) {
        ServiceLoader<GamePlugin> plugins = ServiceLoader.load(GamePlugin.class);

        System.out.println("Assembling components via ServiceLoader:");
        int count = 0;
        for (GamePlugin plugin : plugins) {
            System.out.println("  - " + plugin.start());
            count++;
        }
        System.out.println("Loaded " + count + " component(s).");
    }
}
