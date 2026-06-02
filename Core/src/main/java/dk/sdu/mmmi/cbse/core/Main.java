package dk.sdu.mmmi.cbse.core;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ModuleConfig.class);
        Game game = context.getBean(Game.class);
        game.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
